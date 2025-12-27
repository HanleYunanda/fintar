package com.example.fintar.service;

import com.example.fintar.dto.CustomerDetailRequest;
import com.example.fintar.dto.CustomerDetailResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.User;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.CustomerDetailMapper;
import com.example.fintar.repository.CustomerDetailRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerDetailService {

    private final CustomerDetailRepository customerDetailRepository;
    private final CustomerDetailMapper customerDetailMapper;
    private final UserService userService;

    public List<CustomerDetailResponse> getAllCustomerDetail() {
        List<CustomerDetail> customerDetails = customerDetailRepository.findAll();
        return customerDetailMapper.toResponseList(customerDetails);
    }

    public CustomerDetailResponse createCustomerDetail(CustomerDetailRequest req) {
        // Get user
        User user = userService.getUserEntity(req.getUserId());

        // Each user must only have 1 customer detail
        Optional<CustomerDetail> duplicateCustomerDetail = customerDetailRepository.findByUser(user);
        if(duplicateCustomerDetail.isPresent()) throw new BusinessValidationException("Customer detail for user " + user.getUsername() + " already exist");

        CustomerDetail customerDetail = customerDetailMapper.fromRequest(req);
        customerDetail.setUser(user);
        return customerDetailMapper.toResponse(customerDetailRepository.save(customerDetail));
    }

    public CustomerDetailResponse updateCustomerDetail(UUID id, CustomerDetailRequest req) {
        // Get user
        User user = userService.getUserEntity(req.getUserId());

        // Get customer detail
         CustomerDetail customerDetail = this.getCustomerDetailEntityById(id);

         // Update process
        customerDetail.setFullName(req.getFullName() == null ? customerDetail.getFullName() : req.getFullName());
        customerDetail.setNationalId(req.getNationalId() == null ? customerDetail.getNationalId() : req.getNationalId());
        customerDetail.setCitizenship(req.getCitizenship() == null ? customerDetail.getCitizenship() : req.getCitizenship());
        customerDetail.setPlaceOfBirth(req.getPlaceOfBirth() == null ? customerDetail.getPlaceOfBirth() : req.getPlaceOfBirth());
        customerDetail.setDateOfBirth(req.getDateOfBirth() == null ? customerDetail.getDateOfBirth() : req.getDateOfBirth());
        customerDetail.setIsMale(req.getIsMale() == null ? customerDetail.getIsMale() : req.getIsMale());
        customerDetail.setReligion(req.getReligion() == null ? customerDetail.getReligion() : req.getReligion());
        customerDetail.setMaritalStatus(req.getMaritalStatus() == null ? customerDetail.getMaritalStatus() : req.getMaritalStatus());
        customerDetail.setPhoneNumber(req.getPhoneNumber() == null ? customerDetail.getPhoneNumber() : req.getPhoneNumber());
        customerDetail.setAddress(req.getAddress() == null ? customerDetail.getAddress() : req.getAddress());
        customerDetail.setZipCode(req.getZipCode() == null ? customerDetail.getZipCode() : req.getZipCode());
        customerDetail.setHouseStatus(req.getHouseStatus() == null ? customerDetail.getHouseStatus() : req.getHouseStatus());
        customerDetail.setJob(req.getJob() == null ? customerDetail.getJob() : req.getJob());
        customerDetail.setWorkplace(req.getWorkplace() == null ? customerDetail.getWorkplace() : req.getWorkplace());
        customerDetail.setSalary(req.getSalary() == null ? customerDetail.getSalary() : req.getSalary());
        customerDetail.setAccountNumber(req.getAccountNumber() == null ? customerDetail.getAccountNumber() : req.getAccountNumber());

        return customerDetailMapper.toResponse(customerDetailRepository.save(customerDetail));
    }

    public CustomerDetail getCustomerDetailEntityById(UUID id) {
        Optional<CustomerDetail> customerDetail = customerDetailRepository.findById(id);
        if(customerDetail.isEmpty()) throw new ResourceNotFoundException("Customer detail not found");
        return customerDetail.get();
    }
}
