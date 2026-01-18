package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.ProductRequest;
import com.example.fintar.dto.ProductResponse;
import com.example.fintar.service.ProductService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
//  @PreAuthorize("hasAuthority('READ_PRODUCT')")
  public ResponseEntity<ApiResponse<List<ProductResponse>>> index() {
    List<ProductResponse> products = productService.getAllProduct();
    return ResponseUtil.ok(products, "Successfully get all products");
  }

  @PostMapping
  @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
  public ResponseEntity<ApiResponse<ProductResponse>> create(
      @RequestBody @Valid ProductRequest req) {
    ProductResponse createdProduct = productService.createProduct(req);
    return ResponseUtil.ok(createdProduct, "Successfully create new product");
  }
}
