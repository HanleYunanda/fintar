package com.example.fintar.mapper;

import com.example.fintar.dto.ProductRequest;
import com.example.fintar.dto.ProductResponse;
import com.example.fintar.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final PlafondMapper plafondMapper;

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .plafond(plafondMapper.toResponse(product.getPlafond()))
                .interestRate(product.getInterestRate())
                .tenor(product.getTenor())
                .build();
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    public Product fromRequest(ProductRequest request) {
        return Product.builder()
                .tenor(request.getTenor())
                .interestRate(request.getInterestRate())
                .build();
    }
}
