package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CustomerDetailRequest;
import com.example.fintar.dto.CustomerDetailResponse;
import com.example.fintar.service.CustomerDetailService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer-detail")
@RequiredArgsConstructor
public class CustomerDetailController {

  private final CustomerDetailService customerDetailService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CustomerDetailResponse>>> index() {
    List<CustomerDetailResponse> customerDetails = customerDetailService.getAllCustomerDetail();
    return ResponseUtil.ok(customerDetails, "Successfully get all customer details");
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> create(
      @RequestBody @Valid CustomerDetailRequest req) {
    CustomerDetailResponse createdCustomerDetail = customerDetailService.createCustomerDetail(req);
    return ResponseUtil.ok(createdCustomerDetail, "Successfully create new customer detail");
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<CustomerDetailResponse>> update(
      @RequestBody @Valid CustomerDetailRequest req, @PathVariable UUID id) {
    CustomerDetailResponse updatedCustomerDetail =
        customerDetailService.updateCustomerDetail(id, req);
    return ResponseUtil.ok(updatedCustomerDetail, "Successfully update customer detail");
  }
}
