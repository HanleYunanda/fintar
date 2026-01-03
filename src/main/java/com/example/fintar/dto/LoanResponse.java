package com.example.fintar.dto;

import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

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
    private List<LoanStatusHistoryResponse> loanStatusHistories;
}
