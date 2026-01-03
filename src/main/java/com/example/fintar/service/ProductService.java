package com.example.fintar.service;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.ProductRequest;
import com.example.fintar.dto.ProductResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.Product;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.ProductMapper;
import com.example.fintar.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PlafondService plafondService;

    public List<ProductResponse> getAllProduct() {
        return productMapper.toResponseList(productRepository.findAll());
    }


    public ProductResponse createProduct(ProductRequest req) {
        // Get Plafond by id
        Plafond plafond = plafondService.getPlafondEntity(req.getPlafondId());

        // Check max tenor
        if(req.getTenor() > plafond.getMaxTenor()) {
            throw new BusinessValidationException("Tenor exceeds maximum allowed tenor: " + plafond.getMaxTenor());
        }

        Product product = productMapper.fromRequest(req);
        product.setPlafond(plafond);
        return productMapper.toResponse(productRepository.save(product));
    }

    public Product getProductEntityById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) throw new ResourceNotFoundException("Product with id " + id + " not found");
        return product.get();
    }
}
