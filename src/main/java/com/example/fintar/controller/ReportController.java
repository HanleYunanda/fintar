package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.ApplicationStatusDTO;
import com.example.fintar.dto.BestSellingProductDTO;
import com.example.fintar.dto.DashboardSummaryDTO;
import com.example.fintar.dto.DisbursementTrendDTO;
import com.example.fintar.service.ReportService;
import java.util.List;

import com.example.fintar.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan/report")
@PreAuthorize("hasAuthority('READ_REPORT')")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDTO>> getDashboardSummary() {
        // return ResponseEntity.ok(reportService.getDashboardSummary());
        return ResponseUtil.ok(reportService.getDashboardSummary(), "Successfully get summary report");
    }

    @GetMapping("/disbursement-trends")
    public ResponseEntity<ApiResponse<List<DisbursementTrendDTO>>> getDisbursementTrends() {
        // return ResponseEntity.ok(reportService.getDisbursementTrends());
        return ResponseUtil.ok(reportService.getDisbursementTrends(), "Successfully get disburse trends report");
    }

    @GetMapping("/applications-by-status")
    public ResponseEntity<ApiResponse<List<ApplicationStatusDTO>>> getApplicationsByStatus() {
        return ResponseUtil.ok(reportService.getApplicationsByStatus(),
                "Successfully get applications by status report");
    }

    @GetMapping("/best-selling-products")
    public ResponseEntity<ApiResponse<List<BestSellingProductDTO>>> getBestSellingProducts() {
        return ResponseUtil.ok(reportService.getBestSellingProducts(3), "Successfully get best selling products report");
    }
}
