package com.example.fintar.service;

import com.example.fintar.dto.CustomerDetailRequest;
import com.example.fintar.dto.CustomerDetailResponse;
import com.example.fintar.dto.DocumentResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.User;
import com.example.fintar.entity.UserPrincipal;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.CustomerDetailMapper;
import com.example.fintar.mapper.PlafondMapper;
import com.example.fintar.repository.CustomerDetailRepository;
import com.example.fintar.repository.LoanRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailService {

  private final CustomerDetailRepository customerDetailRepository;
  private final CustomerDetailMapper customerDetailMapper;
  private final UserService userService;
  // private final AuthService authService;
  private final PlafondService plafondService;
  private final PlafondMapper plafondMapper;
  private final LoanRepository loanRepository;

  public List<CustomerDetailResponse> getAllCustomerDetail() {
    List<CustomerDetail> customerDetails = customerDetailRepository.findAll();
    return customerDetailMapper.toResponseList(customerDetails);
  }

  public CustomerDetailResponse createCustomerDetail(CustomerDetailRequest req) {
    // Get user
    User user = userService.getUserEntity(req.getUserId());

    // Each user must only have 1 customer detail
    Optional<CustomerDetail> duplicateCustomerDetail = customerDetailRepository.findByUser(user);
    if (duplicateCustomerDetail.isPresent())
      throw new BusinessValidationException(
          "Customer detail for user " + user.getUsername() + " already exist");

    CustomerDetail customerDetail = customerDetailMapper.fromRequest(req);
    customerDetail.setUser(user);

    // Set basic plafond to new user
    Plafond basicPlafond = plafondService.getPlafondEntityByName("IRON");
    customerDetail.setPlafond(basicPlafond);
    customerDetail.setRemainPlafond(basicPlafond.getMaxAmount());

    return customerDetailMapper.toResponse(customerDetailRepository.save(customerDetail));
  }

  public CustomerDetailResponse updateCustomerDetail(UUID id, CustomerDetailRequest req) {
    // Get user
    User user = userService.getUserEntity(req.getUserId());

    // Get customer detail
    CustomerDetail customerDetail = this.getCustomerDetailEntityById(id);

    // Update process
    customerDetail.setFullName(
        req.getFullName() == null ? customerDetail.getFullName() : req.getFullName());
    customerDetail.setNationalId(
        req.getNationalId() == null ? customerDetail.getNationalId() : req.getNationalId());
    customerDetail.setCitizenship(
        req.getCitizenship() == null ? customerDetail.getCitizenship() : req.getCitizenship());
    customerDetail.setPlaceOfBirth(
        req.getPlaceOfBirth() == null ? customerDetail.getPlaceOfBirth() : req.getPlaceOfBirth());
    customerDetail.setDateOfBirth(
        req.getDateOfBirth() == null ? customerDetail.getDateOfBirth() : req.getDateOfBirth());
    customerDetail.setIsMale(
        req.getIsMale() == null ? customerDetail.getIsMale() : req.getIsMale());
    customerDetail.setReligion(
        req.getReligion() == null ? customerDetail.getReligion() : req.getReligion());
    customerDetail.setMaritalStatus(
        req.getMaritalStatus() == null
            ? customerDetail.getMaritalStatus()
            : req.getMaritalStatus());
    customerDetail.setPhoneNumber(
        req.getPhoneNumber() == null ? customerDetail.getPhoneNumber() : req.getPhoneNumber());
    customerDetail.setAddress(
        req.getAddress() == null ? customerDetail.getAddress() : req.getAddress());
    customerDetail.setZipCode(
        req.getZipCode() == null ? customerDetail.getZipCode() : req.getZipCode());
    customerDetail.setHouseStatus(
        req.getHouseStatus() == null ? customerDetail.getHouseStatus() : req.getHouseStatus());
    customerDetail.setJob(req.getJob() == null ? customerDetail.getJob() : req.getJob());
    customerDetail.setWorkplace(
        req.getWorkplace() == null ? customerDetail.getWorkplace() : req.getWorkplace());
    customerDetail.setSalary(
        req.getSalary() == null ? customerDetail.getSalary() : req.getSalary());
    customerDetail.setAccountNumber(
        req.getAccountNumber() == null
            ? customerDetail.getAccountNumber()
            : req.getAccountNumber());

    return customerDetailMapper.toResponse(customerDetailRepository.save(customerDetail));
  }

  public CustomerDetail getCustomerDetailEntityById(UUID id) {
    Optional<CustomerDetail> customerDetail = customerDetailRepository.findById(id);
    if (customerDetail.isEmpty()) throw new ResourceNotFoundException("Customer detail not found");
    return customerDetail.get();
  }

  public CustomerDetailResponse getCustomerDetailById(UUID id) {
    CustomerDetail customerDetail = this.getCustomerDetailEntityById(id);
    CustomerDetailResponse customerDetailResponse = customerDetailMapper.toResponse(customerDetail);
    if (customerDetail.getPlafond() == null)
      throw new BusinessValidationException("Plafond is not set for this customer");
    customerDetailResponse.setPlafond(plafondMapper.toResponse(customerDetail.getPlafond()));
    customerDetailResponse.setDocuments(
        customerDetail.getDocuments().stream()
            .filter(Document::getIsActive)
            .map(
                document ->
                    DocumentResponse.builder()
                        .id(document.getId())
                        .filename(document.getFileName())
                        .fileUri(document.getFileUri())
                        .contentType(document.getContentType())
                        .size(document.getSize())
                        .docType(document.getDocType())
                        .build())
            .toList());
    return customerDetailResponse;
  }

  public CustomerDetailResponse getCustomerDetailByUserId(UUID userId) {
    User user = userService.getUserEntity(userId);
    if (user.getCustomerDetail() == null)
      throw new BusinessValidationException("Customer detail has not been created");
    CustomerDetail customerDetail = user.getCustomerDetail();
    CustomerDetailResponse customerDetailResponse = customerDetailMapper.toResponse(customerDetail);
    customerDetailResponse.setPlafond(plafondMapper.toResponse(customerDetail.getPlafond()));
    customerDetailResponse.setRemainPlafond(customerDetail.getRemainPlafond());
    return customerDetailResponse;
  }

  public CustomerDetail getCustomerDetailEntityByLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
    User user = userPrincipal.getUser();
    CustomerDetail customerDetail = user.getCustomerDetail();
    if (customerDetail == null) throw new ResourceNotFoundException("Customer detail not found");
    return customerDetail;
  }

  public CustomerDetail substractRemainPlafond(CustomerDetail customerDetail, Long principalDebt) {
    customerDetail.setRemainPlafond(customerDetail.getRemainPlafond() - principalDebt);
    return customerDetailRepository.save(customerDetail);
  }

  public void updatePlafond(CustomerDetail customerDetail, Plafond newPlafond) {
    customerDetail.setPlafond(newPlafond);
    customerDetailRepository.save(customerDetail);
  }

  public void processPlafondUpgrade(UUID userId) {
    User user = userService.getUserEntity(userId);
    CustomerDetail customerDetail = user.getCustomerDetail();

    Plafond currentPlafond = customerDetail.getPlafond();
    Integer maxOrder = plafondService.getMaxOrderNumber();

    if (currentPlafond.getOrderNumber() != null && currentPlafond.getOrderNumber() < maxOrder) {
      Long totalDisbursed = loanRepository.sumDisbursedLoansByUser(userId);

      if (currentPlafond.getNextPlafondLimit() != null
          && totalDisbursed > currentPlafond.getNextPlafondLimit()) {
        Integer nextOrder = currentPlafond.getOrderNumber() + 1;
        Optional<Plafond> nextPlafondOpt = plafondService.getPlafondByOrderNumber(nextOrder);

        if (nextPlafondOpt.isPresent()) {
          this.updatePlafond(customerDetail, nextPlafondOpt.get());
        }
      }
    }
  }
}
