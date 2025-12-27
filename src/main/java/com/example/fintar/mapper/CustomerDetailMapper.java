package com.example.fintar.mapper;

import com.example.fintar.dto.CustomerDetailRequest;
import com.example.fintar.dto.CustomerDetailResponse;
import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Plafond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerDetailMapper {
    private final UserMapper userMapper;

    public CustomerDetailResponse toResponse(CustomerDetail customerDetail) {
        return CustomerDetailResponse.builder()
                .id(customerDetail.getId())
                .user(userMapper.toResponse(customerDetail.getUser()))
                .fullName(customerDetail.getFullName())
                .nationalId(customerDetail.getNationalId())
                .citizenship(customerDetail.getCitizenship())
                .placeOfBirth(customerDetail.getPlaceOfBirth())
                .dateOfBirth(customerDetail.getDateOfBirth())
                .isMale(customerDetail.getIsMale())
                .religion(customerDetail.getReligion())
                .maritalStatus(customerDetail.getMaritalStatus())
                .phoneNumber(customerDetail.getPhoneNumber())
                .address(customerDetail.getAddress())
                .zipCode(customerDetail.getZipCode())
                .houseStatus(customerDetail.getHouseStatus())
                .job(customerDetail.getJob())
                .workplace(customerDetail.getWorkplace())
                .salary(customerDetail.getSalary())
                .accountNumber(customerDetail.getAccountNumber())
                .build();
    }

    public List<CustomerDetailResponse> toResponseList(List<CustomerDetail> customerDetails) {
        return customerDetails.stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerDetail fromRequest(CustomerDetailRequest request) {
        return CustomerDetail.builder()
                .fullName(request.getFullName())
                .nationalId(request.getNationalId())
                .citizenship(request.getCitizenship())
                .placeOfBirth(request.getPlaceOfBirth())
                .dateOfBirth(request.getDateOfBirth())
                .isMale(request.getIsMale())
                .religion(request.getReligion())
                .maritalStatus(request.getMaritalStatus())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .zipCode(request.getZipCode())
                .houseStatus(request.getHouseStatus())
                .job(request.getJob())
                .workplace(request.getWorkplace())
                .salary(request.getSalary())
                .accountNumber(request.getAccountNumber())
                .build();
    }
}
