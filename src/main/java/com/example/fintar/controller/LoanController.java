package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.*;
import com.example.fintar.enums.LoanStatus;
import com.example.fintar.service.LoanService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {

  private final LoanService loanService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<LoanResponse>>> index() {
    List<LoanResponse> loans = loanService.getAllLoan();
    return ResponseUtil.ok(loans, "Successfully get all loans");
  }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_LOAN')")
  public ResponseEntity<ApiResponse<LoanResponse>> createApplication(
      @RequestBody @Valid LoanRequest req) {
    LoanResponse createdLoan = loanService.createLoanApplication(req);
    return ResponseUtil.ok(createdLoan, "Successfully create new loan application");
  }

  @PostMapping("/{id}/review")
  @PreAuthorize("hasAuthority('REVIEW_LOAN')")
  public ResponseEntity<ApiResponse<LoanStatusHistoryResponse>> reviewApplication(
      @RequestBody @Valid ChangeLoanStatusRequest req, @PathVariable UUID id) {
    LoanStatusHistoryResponse isReviewed = loanService.reviewLoanApplication(id, req);
    return ResponseUtil.ok(isReviewed, "Successfully reviewed loan application");
  }

  @PostMapping("/{id}/approval")
  @PreAuthorize("hasAuthority('APPROVE_LOAN')")
  public ResponseEntity<ApiResponse<LoanStatusHistoryResponse>> approveRejectApplication(
      @RequestBody @Valid ChangeLoanStatusRequest req, @PathVariable UUID id) {
    LoanStatusHistoryResponse isApproved = loanService.approveOrRejectLoanApplication(id, req);
    LoanStatus approvalStatus =
        req.getAction() == LoanStatus.APPROVED ? LoanStatus.APPROVED : LoanStatus.REJECTED;
    return ResponseUtil.ok(
        isApproved, "Successfully " + approvalStatus.toString() + " loan application");
  }

  @PostMapping("/{id}/disburse")
  @PreAuthorize("hasAuthority('DISBURSE_LOAN')")
  public ResponseEntity<ApiResponse<LoanStatusHistoryResponse>> disburseApplication(
      @RequestBody @Valid ChangeLoanStatusRequest req, @PathVariable UUID id) {
    LoanStatusHistoryResponse isDisbursed = loanService.disburseLoanApplication(id, req);
    return ResponseUtil.ok(isDisbursed, "Successfully disbursed loan application");
  }
}
