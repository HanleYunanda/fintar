package com.example.fintar.service;

import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.PlafondMapper;
import com.example.fintar.repository.PlafondRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlafondService {

  private final PlafondRepository plafondRepository;
  private final PlafondMapper plafondMapper;

  public List<PlafondResponse> getAllPlafond() {
    return plafondMapper.toResponseList(plafondRepository.findAll());
  }

  @Transactional
  public PlafondResponse createPlafond(PlafondRequest req) {

    Plafond plafond = plafondMapper.fromRequest(req);
    return plafondMapper.toResponse(plafondRepository.save(plafond));
  }

  public Plafond getPlafondEntity(UUID id) {
    Optional<Plafond> plafond = plafondRepository.findById(id);
    if (plafond.isEmpty())
      throw new ResourceNotFoundException("Plafond with id " + id + " not found");
    return plafond.get();
  }

  public PlafondResponse getPlafond(UUID id) {
    return plafondMapper.toResponse(this.getPlafondEntity(id));
  }

  @Transactional
  public PlafondResponse updatePlafond(UUID id, PlafondRequest req) {
    Plafond plafond = this.getPlafondEntity(id);
    plafond.setName(req.getName());
    plafond.setMaxAmount(req.getMaxAmount());
    plafond.setMaxTenor(req.getMaxTenor());
    return plafondMapper.toResponse(plafondRepository.save(plafond));
  }

  @Transactional
  public void deletePlafond(UUID id) {
    Plafond plafond = this.getPlafondEntity(id);
    plafondRepository.delete(plafond);
  }

    public Plafond getPlafondEntityByName(String name) {
      Optional<Plafond> plafond = plafondRepository.findByName(name);
      if (plafond.isEmpty())
        throw new ResourceNotFoundException("Plafond with name " + name + " not found");
      return plafond.get();
    }
}
