package com.example.fintar.dto;

import com.example.fintar.enums.LoanStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanStatusHistoryResponse {
  private UUID id;
  private LoanStatus action;
  private String note;
  private UserResponse performedBy;
  private LocalDateTime performedAt;
}
