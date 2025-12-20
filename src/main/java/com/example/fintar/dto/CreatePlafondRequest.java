package com.example.fintar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CreatePlafondRequest {

    private String name;
    private Double maxAmount;
    private Integer maxTenor;
    private Set<String> productRequest; // String with format <tenor;interestRate>

}
