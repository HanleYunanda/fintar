package com.example.fintar.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Principal Debt is required")
    @Min(value = 0, message = "Principal Debt cannot be negative")
    private Long principalDebt;

    @NotNull(message = "Outstanding Debt is required")
    @Min(value = 0, message = "Outstanding Debt cannot be negative")
    private Long outstandingDebt;

    @NotNull(message = "Tenor is required")
    @Min(value = 1, message = "Tenor must be at least 1 month")
    @Max(value = 48, message = "Tenor must not exceed 48 months")
    private Integer tenor;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Interest rate must be greater than 0")
    @DecimalMax(value = "100.0",
            message = "Interest rate must not exceed 100%")
    private Double interestRate;

    @NotNull(message = "Installment Payment is required")
    @Min(value = 0, message = "Installment Payment cannot be negative")
    private Long installmentPayment;

}
