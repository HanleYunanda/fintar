package com.example.fintar.service;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CreatePlafondRequest;
import com.example.fintar.entity.Plafond;
import com.example.fintar.repository.PlafondRepository;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class PlafondService {

    @Autowired
    private PlafondRepository plafondRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Plafond> getAllPlafond() {
        return plafondRepository.findAll();
    }

    public Plafond createPlafond(CreatePlafondRequest req) {
        // convert ProductRequest to JSON
        String products = objectMapper.writeValueAsString(req.getProductRequest());

        Plafond plafond = Plafond.builder()
                .name(req.getName())
                .maxAmount(req.getMaxAmount())
                .maxTenor(req.getMaxTenor())
                .productSelection(products)
                .build();
        return plafondRepository.save(plafond);
    }
}
