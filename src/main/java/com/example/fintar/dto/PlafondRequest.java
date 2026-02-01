package com.example.fintar.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlafondRequest {
  @NotNull(message = "Name is required")
  @NotBlank(message = "Name cannot be blank")
  private String name;

  @NotNull(message = "Max amount is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Max amount must be greater than 0")
  private Double maxAmount;

  @NotNull(message = "Max tenor is required")
  @Min(value = 1, message = "Max tenor be at least 1 month")
  private Integer maxTenor;
}
