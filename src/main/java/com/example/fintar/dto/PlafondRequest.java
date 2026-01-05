package com.example.fintar.dto;

import lombok.Data;

@Data
public class PlafondRequest {
  private String name;
  private Double maxAmount;
  private Integer maxTenor;
}
