package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.service.PlafondService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plafond")
@RequiredArgsConstructor
public class PlafondController {

  private final PlafondService plafondService;

  @GetMapping
//  @PreAuthorize("hasAuthority('READ_PLAFOND')")
  public ResponseEntity<ApiResponse<List<PlafondResponse>>> index() {
    List<PlafondResponse> plafonds = plafondService.getAllPlafond();
    return ResponseUtil.ok(plafonds, "Successfully get all plafonds");
  }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_PLAFOND')")
  public ResponseEntity<ApiResponse<PlafondResponse>> create(
      @RequestBody @Valid PlafondRequest req) {
    PlafondResponse createdPlafond = plafondService.createPlafond(req);
    return ResponseUtil.ok(createdPlafond, "Successfully create new plafond");
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('READ_PLAFOND')")
  public ResponseEntity<ApiResponse<PlafondResponse>> show(@PathVariable UUID id) {
    PlafondResponse plafond = plafondService.getPlafond(id);
    return ResponseUtil.ok(plafond, "Successfully get plafond");
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('UPDATE_PLAFOND')")
  public ResponseEntity<ApiResponse<PlafondResponse>> update(
      @PathVariable UUID id, @RequestBody @Valid PlafondRequest req) {
    PlafondResponse plafond = plafondService.updatePlafond(id, req);
    return ResponseUtil.ok(plafond, "Successfully update plafond");
  }

//  @DeleteMapping("/{id}")
//  @PreAuthorize("hasAuthority('DELETE_PLAFOND')")
//  public ResponseEntity<ApiResponse<Plafond>> delete(@PathVariable UUID id) {
//    plafondService.deletePlafond(id);
//    return ResponseUtil.ok(null, "Successfully delete plafond");
//  }
}
