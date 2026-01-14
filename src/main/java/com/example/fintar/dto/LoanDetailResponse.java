package com.example.fintar.dto;

import com.example.fintar.entity.Document;
import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LoanDetailResponse {
    private LoanResponse loan;
    private List<LoanStatusHistoryResponse> loanStatusHistories;
    private CustomerDetailResponse customerDetail;
    private List<DocumentResponse> documents;
}
