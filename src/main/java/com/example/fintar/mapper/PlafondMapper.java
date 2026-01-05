package com.example.fintar.mapper;

import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PlafondMapper {
  public PlafondResponse toResponse(Plafond plafond) {
    return PlafondResponse.builder()
        .id(plafond.getId())
        .name(plafond.getName())
        .maxAmount(plafond.getMaxAmount())
        .maxTenor(plafond.getMaxTenor())
        .build();
  }

  public List<PlafondResponse> toResponseList(List<Plafond> plafonds) {
    return plafonds.stream().map(this::toResponse).toList();
  }

  public Plafond fromRequest(PlafondRequest request) {
    return Plafond.builder()
        .name(request.getName())
        .maxAmount(request.getMaxAmount())
        .maxTenor(request.getMaxTenor())
        .build();
  }
}
