package com.example.fintar.mapper;

import com.example.fintar.dto.LoanRequest;
import com.example.fintar.dto.LoanResponse;
import com.example.fintar.dto.LoanStatusHistoryResponse;
import com.example.fintar.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanMapper {

    private final ProductMapper productMapper;
    private final LoanStatusHistoryMapper loanStatusHistoryMapper;

    public LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .product(productMapper.toResponse(loan.getProduct()))
                .principalDebt(loan.getPrincipalDebt())
                .outstandingDebt(loan.getOutstandingDebt())
                .tenor(loan.getTenor())
                .interestRate(loan.getInterestRate())
                .installmentPayment(loan.getInstallmentPayment())
                .status(loan.getStatus())
                .loanStatusHistories(loanStatusHistoryMapper.toResponseList(loan.getStatusHistories()))
                .build();
    }

    public List<LoanResponse> toResponseList(List<Loan> loans) {
        return loans.stream()
                .map(this::toResponse)
                .toList();
    }

    public Loan fromRequest(LoanRequest request) {
        return Loan.builder()
                .build();
    }
}
