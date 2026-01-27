package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.*;
import com.example.fintar.entity.*;
import com.example.fintar.enums.LoanStatus;
import com.example.fintar.mapper.CustomerDetailMapper;
import com.example.fintar.mapper.LoanMapper;
import com.example.fintar.mapper.LoanStatusHistoryMapper;
import com.example.fintar.service.LoanService;
import com.example.fintar.service.UserService;
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
  private final LoanMapper loanMapper;
  private final LoanStatusHistoryMapper loanStatusHistoryMapper;
  private final CustomerDetailMapper customerDetailMapper;

  @GetMapping
  @PreAuthorize("hasAuthority('READ_LOAN')")
  public ResponseEntity<ApiResponse<List<LoanResponse>>> index() {
    List<LoanResponse> loans = loanService.getAllLoan();
    return ResponseUtil.ok(loans, "Successfully get all loans");
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('READ_LOAN')")
  public ResponseEntity<ApiResponse<LoanDetailResponse>> show(
          @PathVariable UUID id
  ) {
    Loan loan = loanService.getLoanEntityById(id);
    List<LoanStatusHistory> loanStatusHistories = loan.getStatusHistories();
    CustomerDetail customerDetail = loanService.getCustomerDetailEntityForLoan(loan);
    List<Document> documents = loan.getDocuments();
    LoanDetailResponse loanDetailResponse = LoanDetailResponse.builder()
            .loan(loanMapper.toResponse(loan))
            .loanStatusHistories(loanStatusHistoryMapper.toResponseList(loanStatusHistories))
            .customerDetail(customerDetailMapper.toResponse(customerDetail))
            .documents(
                    documents.stream()
                            .map(document -> DocumentResponse.builder()
                                    .id(document.getId())
                                    .filename(document.getFileName())
                                    .fileUri(document.getFileUri())
                                    .docType(document.getDocType())
                                    .contentType(document.getContentType())
                                    .build())
                            .toList()
            )
            .build();
    return ResponseUtil.ok(loanDetailResponse, "Successfully get loan detail");
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasAuthority('READ_LOAN')")
  public ResponseEntity<ApiResponse<List<LoanResponse>>> allLoanByUser(
          @PathVariable UUID userId
  ) {
    List<LoanResponse> loanResponses = loanService.getAllLoanByUserId(userId);
    return ResponseUtil.ok(loanResponses, "Successfully get loan");
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
