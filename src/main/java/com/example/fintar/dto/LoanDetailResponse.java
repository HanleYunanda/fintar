package com.example.fintar.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDetailResponse {
  private LoanResponse loan;
  private List<LoanStatusHistoryResponse> loanStatusHistories;
  private CustomerDetailResponse customerDetail;
  private List<DocumentResponse> documents;
}
