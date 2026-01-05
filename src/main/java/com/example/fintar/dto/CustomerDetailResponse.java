package com.example.fintar.dto;

import com.example.fintar.enums.HouseStatus;
import com.example.fintar.enums.MaritalStatus;
import com.example.fintar.enums.Religion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetailResponse {

  private UUID id;
  private UserResponse user;
  private String fullName;
  private String nationalId;
  private String citizenship;
  private String placeOfBirth;
  private LocalDateTime dateOfBirth;
  private Boolean isMale;

  @Enumerated(EnumType.STRING)
  private Religion religion;

  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  private String phoneNumber;
  private String address;
  private String zipCode;

  @Enumerated(EnumType.STRING)
  private HouseStatus houseStatus;

  private String job;
  private String workplace;
  private Double salary;
  private String accountNumber;
}
