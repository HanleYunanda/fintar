package com.example.fintar.mapper;

import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.RoleRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return plafonds.stream()
                .map(this::toResponse)
                .toList();
    }

    public Plafond fromRequest(PlafondRequest request) {
        return Plafond.builder()
                .name(request.getName())
                .maxAmount(request.getMaxAmount())
                .maxTenor(request.getMaxTenor())
                .build();
    }
}
