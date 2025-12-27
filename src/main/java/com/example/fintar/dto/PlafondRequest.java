package com.example.fintar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class PlafondRequest {
    private String name;
    private Double maxAmount;
    private Integer maxTenor;
}
