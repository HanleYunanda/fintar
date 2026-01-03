package com.example.fintar.dto;

import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeLoanStatusRequest {
    private LoanStatus action;
    private String note;
}
