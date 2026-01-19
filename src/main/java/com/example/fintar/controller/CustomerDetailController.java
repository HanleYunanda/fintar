package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CustomerDetailRequest;
import com.example.fintar.dto.CustomerDetailResponse;
import com.example.fintar.entity.User;
import com.example.fintar.service.CustomerDetailService;
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
@RequestMapping("/customer-detail")
@RequiredArgsConstructor
public class CustomerDetailController {

  private final CustomerDetailService customerDetailService;
  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasAuthority('READ_CUSTOMER_DETAIL')")
  public ResponseEntity<ApiResponse<List<CustomerDetailResponse>>> index() {
    List<CustomerDetailResponse> customerDetails = customerDetailService.getAllCustomerDetail();
    return ResponseUtil.ok(customerDetails, "Successfully get all customer details");
  }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_CUSTOMER_DETAIL')")
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> create(
      @RequestBody @Valid CustomerDetailRequest req) {
    CustomerDetailResponse createdCustomerDetail = customerDetailService.createCustomerDetail(req);
    return ResponseUtil.ok(createdCustomerDetail, "Successfully create new customer detail");
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAuthority('UPDATE_CUSTOMER_DETAIL')")
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> update(
      @RequestBody @Valid CustomerDetailRequest req, @PathVariable UUID id) {
    CustomerDetailResponse updatedCustomerDetail =
        customerDetailService.updateCustomerDetail(id, req);
    return ResponseUtil.ok(updatedCustomerDetail, "Successfully update customer detail");
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> show(
          @PathVariable UUID id
  ) {
    CustomerDetailResponse customerDetailResponse = customerDetailService.getCustomerDetailById(id);
    return ResponseUtil.ok(customerDetailResponse, "Successfully get customer detail");
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> getUserCustomerDetail(
          @PathVariable UUID userId
  ) {
    CustomerDetailResponse customerDetailResponse = customerDetailService.getCustomerDetailByUserId(userId);
    return ResponseUtil.ok(customerDetailResponse, "Successfully get customer detail");
  }

}
