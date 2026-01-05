package com.example.fintar.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductRequest {

  @NotNull(message = "Plafond ID is required")
  private UUID plafondId;

  @NotNull(message = "Tenor is required")
  @Min(value = 1, message = "Tenor must be at least 1 month")
  @Max(value = 48, message = "Tenor must not exceed 48 months")
  private Integer tenor;

  @NotNull(message = "Interest rate is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than 0")
  @DecimalMax(value = "100.0", message = "Interest rate must not exceed 100%")
  private Double interestRate;
}
