package com.example.fintar.dto;

import com.example.fintar.entity.Loan;
import com.example.fintar.entity.User;
import com.example.fintar.enums.LoanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LoanStatusHistoryResponse {
    private UUID id;
    private LoanStatus action;
    private String note;
    private UserResponse performedBy;
    private LocalDateTime performedAt;
}
