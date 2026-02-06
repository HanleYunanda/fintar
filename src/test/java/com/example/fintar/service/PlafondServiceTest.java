package com.example.fintar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.fintar.dto.PlafondOrderRequest;
import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.PlafondMapper;
import com.example.fintar.repository.PlafondRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlafondServiceTest {

  @Mock private PlafondRepository plafondRepository;

  @Mock private PlafondMapper plafondMapper;

  @InjectMocks private PlafondService plafondService;

  @Test
  void testGetAllPlafond() {
    Plafond plafond = new Plafond();
    PlafondResponse response = PlafondResponse.builder().build();
    when(plafondRepository.findAll()).thenReturn(Collections.singletonList(plafond));
    when(plafondMapper.toResponseList(any())).thenReturn(Collections.singletonList(response));

    List<PlafondResponse> result = plafondService.getAllPlafond();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(plafondRepository, times(1)).findAll();
  }

  @Test
  void testCreatePlafond() {
    PlafondRequest request = new PlafondRequest();
    Plafond plafond = new Plafond();
    PlafondResponse response = PlafondResponse.builder().build();

    when(plafondMapper.fromRequest(request)).thenReturn(plafond);
    when(plafondRepository.save(plafond)).thenReturn(plafond);
    when(plafondMapper.toResponse(plafond)).thenReturn(response);

    PlafondResponse result = plafondService.createPlafond(request);

    assertNotNull(result);
    verify(plafondRepository, times(1)).save(plafond);
  }

  @Test
  void testGetPlafondEntity_Success() {
    UUID id = UUID.randomUUID();
    Plafond plafond = new Plafond();
    when(plafondRepository.findById(id)).thenReturn(Optional.of(plafond));

    Plafond result = plafondService.getPlafondEntity(id);

    assertNotNull(result);
    assertEquals(plafond, result);
  }

  @Test
  void testGetPlafondEntity_NotFound() {
    UUID id = UUID.randomUUID();
    when(plafondRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> plafondService.getPlafondEntity(id));
  }

  @Test
  void testGetPlafond() {
    UUID id = UUID.randomUUID();
    Plafond plafond = new Plafond();
    PlafondResponse response = PlafondResponse.builder().build();

    when(plafondRepository.findById(id)).thenReturn(Optional.of(plafond));
    when(plafondMapper.toResponse(plafond)).thenReturn(response);

    PlafondResponse result = plafondService.getPlafond(id);

    assertNotNull(result);
  }

  @Test
  void testUpdatePlafond() {
    UUID id = UUID.randomUUID();
    PlafondRequest request = new PlafondRequest();
    request.setName("New Name");
    request.setMaxAmount(10.0);
    request.setMaxTenor(12);
    request.setNextPlafondLimit(1.0);

    Plafond plafond = new Plafond();
    PlafondResponse response = PlafondResponse.builder().build();

    when(plafondRepository.findById(id)).thenReturn(Optional.of(plafond)); // For getPlafondEntity
    when(plafondRepository.save(plafond)).thenReturn(plafond);
    when(plafondMapper.toResponse(plafond)).thenReturn(response);

    PlafondResponse result = plafondService.updatePlafond(id, request);

    assertNotNull(result);
    assertEquals("New Name", plafond.getName());
    assertEquals(10.0, plafond.getMaxAmount());
    assertEquals(12, plafond.getMaxTenor());
    assertEquals(1.0, plafond.getNextPlafondLimit());
    verify(plafondRepository, times(1)).save(plafond);
  }

  @Test
  void testDeletePlafond() {
    UUID id = UUID.randomUUID();
    Plafond plafond = new Plafond();
    when(plafondRepository.findById(id)).thenReturn(Optional.of(plafond));

    plafondService.deletePlafond(id);

    verify(plafondRepository, times(1)).delete(plafond);
  }

  @Test
  void testGetPlafondEntityByName_Success() {
    String name = "Test Plafond";
    Plafond plafond = new Plafond();
    when(plafondRepository.findByName(name)).thenReturn(Optional.of(plafond));

    Plafond result = plafondService.getPlafondEntityByName(name);

    assertNotNull(result);
    assertEquals(plafond, result);
  }

  @Test
  void testGetPlafondEntityByName_NotFound() {
    String name = "Non Existent";
    when(plafondRepository.findByName(name)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class, () -> plafondService.getPlafondEntityByName(name));
  }

  @Test
  void testUpdatePlafondOrders() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    PlafondOrderRequest req1 = new PlafondOrderRequest();
    req1.setId(id1);
    req1.setOrderNumber(1);

    PlafondOrderRequest req2 = new PlafondOrderRequest();
    req2.setId(id2);
    req2.setOrderNumber(2);

    List<PlafondOrderRequest> requests = Arrays.asList(req1, req2);

    Plafond p1 = new Plafond();
    p1.setId(id1);
    Plafond p2 = new Plafond();
    p2.setId(id2);
    List<Plafond> plafonds = Arrays.asList(p1, p2);

    when(plafondRepository.findAll()).thenReturn(plafonds);
    when(plafondRepository.saveAll(any())).thenReturn(plafonds);
    PlafondResponse resp1 = PlafondResponse.builder().build();
    PlafondResponse resp2 = PlafondResponse.builder().build();
    when(plafondMapper.toResponseList(any())).thenReturn(Arrays.asList(resp1, resp2));

    List<PlafondResponse> result = plafondService.updatePlafondOrders(requests);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(plafondRepository, times(1)).saveAll(plafonds);
    assertEquals(1, p1.getOrderNumber());
    assertEquals(2, p2.getOrderNumber());
  }

  @Test
  void testGetActivePlafonds() {
    Plafond plafond = new Plafond();
    PlafondResponse response = PlafondResponse.builder().build();

    when(plafondRepository.findByOrderNumberIsNotNullOrderByOrderNumberAsc())
        .thenReturn(Collections.singletonList(plafond));
    when(plafondMapper.toResponseList(any())).thenReturn(Collections.singletonList(response));

    List<PlafondResponse> result = plafondService.getActivePlafonds();

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testGetPlafondByOrderNumber() {
    Integer orderNumber = 1;
    Plafond plafond = new Plafond();
    when(plafondRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(plafond));

    Optional<Plafond> result = plafondService.getPlafondByOrderNumber(orderNumber);

    assertTrue(result.isPresent());
    assertEquals(plafond, result.get());
  }

  @Test
  void testGetMaxOrderNumber() {
    Integer maxOrder = 5;
    when(plafondRepository.findMaxOrderNumber()).thenReturn(maxOrder);

    Integer result = plafondService.getMaxOrderNumber();

    assertEquals(maxOrder, result);
  }
}
