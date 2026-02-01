package com.example.fintar.service;

import com.example.fintar.dto.ChangeLoanStatusRequest;
import com.example.fintar.dto.LoanRequest;
import com.example.fintar.dto.LoanResponse;
import com.example.fintar.dto.LoanStatusHistoryResponse;
import com.example.fintar.entity.*;
import com.example.fintar.enums.LoanStatus;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.LoanMapper;
import com.example.fintar.mapper.LoanStatusHistoryMapper;
import com.example.fintar.repository.LoanRepository;
import com.example.fintar.repository.LoanStatusHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository loanRepository;
  private final LoanStatusHistoryRepository loanStatusHistoryRepository;
  private final ProductService productService;
  private final LoanMapper loanMapper;
  private final LoanStatusHistoryMapper loanStatusHistoryMapper;
  private final UserService userService;
  private final DocumentService documentService;
  private final CustomerDetailService customerDetailService;
  private final NotificationService notificationService;

  public List<LoanResponse> getAllLoan() {
    return loanMapper.toResponseList(loanRepository.findAll());
  }

  @Transactional
  public LoanResponse createLoanApplication(LoanRequest req) {

    // Get product by id
    Product product = productService.getProductEntityById(req.getProductId());
    // Get plafond
    Plafond plafond = product.getPlafond();

    // Check if principal debt submitted is less than max amount allowed
    if (req.getPrincipalDebt() > plafond.getMaxAmount())
      throw new BusinessValidationException("Loan exceeding the permitted limit");

    // Check if tenor submitted is less than max tenor allowed
    if (req.getTenor() > plafond.getMaxTenor())
      throw new BusinessValidationException("Tenor exceeding the permitted limit");

    // Check simulation result
    Long interestAmount = Math.round(req.getPrincipalDebt() * (req.getInterestRate() / 100) * req.getTenor());
    Long outstandingDebt = req.getPrincipalDebt() + interestAmount;
    Long installmentPayment = outstandingDebt / req.getTenor();

    if (!outstandingDebt.equals(req.getOutstandingDebt()))
      throw new BusinessValidationException("Incorrect calculation of outstanding debt");
    if (!installmentPayment.equals(req.getInstallmentPayment()))
      throw new BusinessValidationException("Incorrect calculation of installment payment");

    // Get logged in user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

    // Get Customer detail
    CustomerDetail customerDetail = userPrincipal.getUser().getCustomerDetail();
    // Check : if there are null field in customer detail
    if (customerDetail == null
        || customerDetail.getFullName() == null
        || customerDetail.getFullName().isBlank()
        || customerDetail.getNationalId() == null
        || customerDetail.getNationalId().isBlank()
        || customerDetail.getCitizenship() == null
        || customerDetail.getCitizenship().isBlank()
        || customerDetail.getPlaceOfBirth() == null
        || customerDetail.getPlaceOfBirth().isBlank()
        || customerDetail.getDateOfBirth() == null
        || customerDetail.getIsMale() == null
        || customerDetail.getReligion() == null
        || customerDetail.getMaritalStatus() == null
        || customerDetail.getPhoneNumber() == null
        || customerDetail.getPhoneNumber().isBlank()
        || customerDetail.getAddress() == null
        || customerDetail.getAddress().isBlank()
        || customerDetail.getZipCode() == null
        || customerDetail.getZipCode().isBlank()
        || customerDetail.getHouseStatus() == null
        || customerDetail.getJob() == null
        || customerDetail.getJob().isBlank()
        || customerDetail.getWorkplace() == null
        || customerDetail.getWorkplace().isBlank()
        || customerDetail.getSalary() == null
        || customerDetail.getAccountNumber() == null
        || customerDetail.getAccountNumber().isBlank()) {
      throw new BusinessValidationException("Customer detail is not completed");
    }

    // Check if customer's plafond still remain
    if (customerDetail.getRemainPlafond() < req.getPrincipalDebt())
      throw new BusinessValidationException("Your plafond is insufficient");

    // Get all doc for this loan
    List<Document> documents = documentService.getDocumentEntitiesByCustomerDetail(customerDetail);

    // Create loan
    Loan loan = Loan.builder()
        .product(product)
        .principalDebt(req.getPrincipalDebt())
        .outstandingDebt(req.getOutstandingDebt())
        .tenor(req.getTenor())
        .interestRate(req.getInterestRate())
        .installmentPayment(req.getInstallmentPayment())
        .status(LoanStatus.CREATED)
        .documents(documents)
        .latitude(req.getLatitude())
        .longitude(req.getLongitude())
        .build();
    loan = loanRepository.save(loan);

    // Create loan status history
    LoanStatusHistory loanStatusHistory = LoanStatusHistory.builder()
        .loan(loan)
        .action(LoanStatus.CREATED)
        .performedBy(userPrincipal.getUser())
        .performedAt(LocalDateTime.now())
        .build();
    loanStatusHistory = loanStatusHistoryRepository.save(loanStatusHistory);

    loan.setStatusHistories(this.getLoanStatusHistoriesEntityByLoan(loan.getId()));
    // loan = this.getLoanEntityById(loan.getId());

    // Substract remain plafond
    customerDetailService.substractRemainPlafond(customerDetail, loan.getPrincipalDebt());

    // Send Notification
    if (userPrincipal.getUser().getFcmToken() != null) {
      notificationService.sendNotification(
          userPrincipal.getUser().getFcmToken(),
          "Loan Created",
          "Your loan application has been successfully created.");
    }

    return loanMapper.toResponse(loan);
  }

  public Loan getLoanEntityById(UUID id) {
    Optional<Loan> loan = loanRepository.findById(id);
    if (loan.isEmpty())
      throw new ResourceNotFoundException("Loan with id " + id + " not found");
    return loan.get();
  }

  public List<LoanStatusHistory> getLoanStatusHistoriesEntityByLoan(UUID loanId) {
    List<LoanStatusHistory> loanStatusHistories = loanStatusHistoryRepository.findByLoanId(loanId);
    return loanStatusHistories;
  }

  public CustomerDetail getCustomerDetailEntityForLoan(Loan loan) {
    User user = userService.getUserEntity(loan.getCreatedBy());
    return user.getCustomerDetail();
  }

  @Transactional
  public LoanStatusHistoryResponse reviewLoanApplication(UUID id, ChangeLoanStatusRequest req) {
    // Check : action must be REVIEWED
    if (req.getAction() != LoanStatus.REVIEWED)
      throw new BusinessValidationException("Wrong action input");

    // Get loan
    Loan loan = this.getLoanEntityById(id);

    // Check : status must be created
    if (loan.getStatus() != LoanStatus.CREATED)
      throw new BusinessValidationException("Loan application has not been created");

    return loanStatusHistoryMapper.toResponse(this.changeStatusApproval(loan, req));
  }

  @Transactional
  public LoanStatusHistoryResponse approveOrRejectLoanApplication(
      UUID id, ChangeLoanStatusRequest req) {
    // Check : action must be APPROVED or REJECTED
    if (req.getAction() != LoanStatus.APPROVED && req.getAction() != LoanStatus.REJECTED)
      throw new BusinessValidationException("Wrong action input");

    // Get loan
    Loan loan = this.getLoanEntityById(id);

    if (loan.getStatus() != LoanStatus.REVIEWED)
      throw new BusinessValidationException("Loan application has not been reviewed");

    return loanStatusHistoryMapper.toResponse(this.changeStatusApproval(loan, req));
  }

  public LoanStatusHistoryResponse disburseLoanApplication(UUID id, ChangeLoanStatusRequest req) {
    // Check : action must be DISBURSED
    if (req.getAction() != LoanStatus.DISBURSED)
      throw new BusinessValidationException("Wrong action input");

    // Get loan
    Loan loan = this.getLoanEntityById(id);

    // Check : loan must be approved first
    if (loan.getStatus() != LoanStatus.APPROVED)
      throw new BusinessValidationException("Loan application has not been approved");

    return loanStatusHistoryMapper.toResponse(this.changeStatusApproval(loan, req));
  }

  public LoanStatusHistory changeStatusApproval(Loan loan, ChangeLoanStatusRequest req) {
    // Get logged in user
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

    // Create new status history
    LoanStatusHistory loanStatusHistory = LoanStatusHistory.builder()
        .loan(loan)
        .action(req.getAction())
        .note(req.getNote())
        .performedBy(userPrincipal.getUser())
        .performedAt(LocalDateTime.now())
        .build();
    loan.setStatus(req.getAction());
    loanRepository.save(loan);

    // Send notification to the loan owner
    User owner = userService.getUserEntity(loan.getCreatedBy());
    if (owner.getFcmToken() != null) {
      notificationService.sendNotification(
          owner.getFcmToken(),
          "Loan Status Update",
          "Your loan status has been updated to " + req.getAction());
    }

    return loanStatusHistoryRepository.save(loanStatusHistory);
  }

  public LoanResponse getLoanById(UUID id) {
    return loanMapper.toResponse(this.getLoanEntityById(id));
  }

  public List<LoanResponse> getAllLoanByUserId(UUID userId) {
    return loanMapper.toResponseList(loanRepository.findAllByCreatedBy(userId));
  }

}
