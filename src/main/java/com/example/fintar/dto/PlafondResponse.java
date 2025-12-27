package com.example.fintar.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PlafondResponse {
    private UUID id;
    private String name;
    private Double maxAmount;
    private Integer maxTenor;
}
