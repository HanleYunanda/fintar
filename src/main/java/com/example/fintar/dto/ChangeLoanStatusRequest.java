package com.example.fintar.dto;

import com.example.fintar.enums.LoanStatus;
import lombok.Data;

@Data
public class ChangeLoanStatusRequest {
  private LoanStatus action;
  private String note;
}
