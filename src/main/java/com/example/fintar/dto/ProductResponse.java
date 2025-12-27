package com.example.fintar.dto;

import com.example.fintar.entity.Plafond;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private PlafondResponse plafond;
    private Integer tenor;
    private Double interestRate;
}
