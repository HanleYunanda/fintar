package com.example.fintar.dto;

import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanResponse {
  private UUID id;
  private ProductResponse product;
  private Long principalDebt;
  private Long outstandingDebt;
  private Integer tenor;
  private Double interestRate;
  private Long installmentPayment;

  @Enumerated(EnumType.STRING)
  private LoanStatus status;

  private Double latitude;

  private Double longitude;

  private LocalDateTime createdAt;

  private List<LoanStatusHistoryResponse> loanStatusHistories;
}
