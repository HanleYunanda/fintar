package com.example.fintar.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
  private UUID id;
  private PlafondResponse plafond;
  private Integer tenor;
  private Double interestRate;
}
