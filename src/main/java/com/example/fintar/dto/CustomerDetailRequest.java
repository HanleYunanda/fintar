package com.example.fintar.dto;

import com.example.fintar.enums.HouseStatus;
import com.example.fintar.enums.MaritalStatus;
import com.example.fintar.enums.Religion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetailRequest {

  @NotNull(message = "User ID is required")
  private UUID userId;

  @Size(max = 150, message = "Full name must not exceed 150 characters")
  private String fullName;

  @Pattern(regexp = "^[0-9]{16}$", message = "National ID must be 16 digits")
  private String nationalId;

  @Size(max = 50)
  private String citizenship;

  @Size(max = 100)
  private String placeOfBirth;

  @Past(message = "Date of birth must be in the past")
  private LocalDate dateOfBirth;

  @NotNull(message = "Gender is required")
  private Boolean isMale;

  @Enumerated(EnumType.STRING)
  private Religion religion;

  @Enumerated(EnumType.STRING)
  private MaritalStatus maritalStatus;

  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number is invalid")
  private String phoneNumber;

  @Size(max = 255)
  private String address;

  @Pattern(regexp = "^[0-9]{5}$", message = "Zip code must be 5 digits")
  private String zipCode;

  @Enumerated(EnumType.STRING)
  private HouseStatus houseStatus;

  @Size(max = 100)
  private String job;

  @Size(max = 150)
  private String workplace;

  @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
  private Double salary;

  private String accountNumber;
}
