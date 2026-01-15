package com.example.fintar.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private Long totalApplications;
    private Double totalApplicationsGrowth; // Percentage vs last month

    private BigDecimal totalOutstanding; // Current Total Outstanding or Monthly Disbursement? Assuming Total
                                         // Outstanding Portfolio.
    private Double totalOutstandingGrowth;

    private Long activeLoans;

    private Double approvalRate;
}
