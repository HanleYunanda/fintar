package com.example.fintar.service;

import com.example.fintar.dto.ApplicationStatusDTO;
import com.example.fintar.dto.BestSellingProductDTO;
import com.example.fintar.dto.DashboardSummaryDTO;
import com.example.fintar.dto.DisbursementTrendDTO;
import com.example.fintar.repository.LoanRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final LoanRepository loanRepository;

  public DashboardSummaryDTO getDashboardSummary() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

    LocalDateTime startOfLastMonth =
        now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    LocalDateTime endOfLastMonth =
        now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

    // 1. Total Applications (Month)
    long totalAppsThisMonth = loanRepository.countApplicationsBetween(startOfMonth, endOfMonth);
    long totalAppsLastMonth =
        loanRepository.countApplicationsBetween(startOfLastMonth, endOfLastMonth);
    double appsGrowth = calculateGrowth(totalAppsThisMonth, totalAppsLastMonth);

    // 2. Total Outstanding (Portfolio)
    // Assuming Card 2 is Total Portfolio Outstanding (Snapshot)
    // We cannot calculate growth accurately without historical snapshot table, so
    // we use 0 or mock.
    // Alternative: Interpretation "Total Outstanding (Month)" = "New Disbursements
    // this month".
    // Given visual cues (green, vs last month), let's implement "New Disbursements"
    // growth for the % but return Total Portfolio for the value?
    // That would be confusing.
    // Let's implement Total Outstanding Value. Growth = 0 (or TODO).
    Long totalOutstandingVal = loanRepository.sumTotalOutstandingActive(startOfMonth, endOfMonth);
    BigDecimal totalOutstanding =
        totalOutstandingVal != null ? BigDecimal.valueOf(totalOutstandingVal) : BigDecimal.ZERO;

    // As a proxy for growth, we can check "Net Change" but that's complex.
    // Let's leave growth as 0 for Total Outstanding as we lack historical data.
    double outstandingGrowth = 0.0;

    // 3. Active Loans
    long activeLoans = loanRepository.countActiveLoans(startOfMonth, endOfMonth);

    // 4. Approval Rate (This Month)
    long approvedThisMonth =
        loanRepository.countApprovedApplicationsBetween(startOfMonth, endOfMonth);
    double approvalRate =
        totalAppsThisMonth > 0 ? ((double) approvedThisMonth / totalAppsThisMonth) * 100 : 0.0;

    return DashboardSummaryDTO.builder()
        .totalApplications(totalAppsThisMonth)
        .totalApplicationsGrowth(appsGrowth)
        .totalOutstanding(totalOutstanding)
        .totalOutstandingGrowth(outstandingGrowth)
        .activeLoans(activeLoans)
        .approvalRate(approvalRate)
        .build();
  }

  public List<DisbursementTrendDTO> getDisbursementTrends() {
    // Last 6 months
    LocalDateTime sixMonthsAgo =
        LocalDateTime.now()
            .minusMonths(6)
            .with(TemporalAdjusters.firstDayOfMonth())
            .with(LocalTime.MIN);
    // End of this month
    LocalDateTime endOfThisMonth =
        LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

    return loanRepository.findDisbursementTrendsAfter(sixMonthsAgo, endOfThisMonth);
  }

  private double calculateGrowth(long current, long previous) {
    if (previous == 0) {
      return current > 0 ? 100.0 : 0.0;
    }
    return ((double) (current - previous) / previous) * 100;
  }

  public List<ApplicationStatusDTO> getApplicationsByStatus() {
    return loanRepository.countApplicationsByStatus();
  }

  public List<BestSellingProductDTO> getBestSellingProducts(Integer limit) {
    return loanRepository.findBestSellingProducts(limit);
  }
}
