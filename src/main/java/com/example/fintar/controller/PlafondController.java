package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.dto.UserRequest;
import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.User;
import com.example.fintar.service.PlafondService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plafonds")
public class PlafondController {

    @Autowired
    private PlafondService plafondService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlafondResponse>>> index() {
        List<PlafondResponse> plafonds = plafondService.getAllPlafond();
        return ResponseUtil.ok(plafonds, "Successfully get all plafonds");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlafondResponse>> create(
            @RequestBody @Valid PlafondRequest req
    ) {
        PlafondResponse createdPlafond = plafondService.createPlafond(req);
        return ResponseUtil.ok(createdPlafond, "Successfully create new user");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlafondResponse>> show(
            @PathVariable UUID id
    ) {
        PlafondResponse plafond = plafondService.getPlafond(id);
        return ResponseUtil.ok(plafond, "Successfully get plafond");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlafondResponse>> update(
            @PathVariable UUID id,
            @RequestBody @Valid PlafondRequest req
    ) {
        PlafondResponse plafond = plafondService.updatePlafond(id, req);
        return ResponseUtil.ok(plafond, "Successfully update plafond");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Plafond>> delete(
            @PathVariable UUID id
    ) {
        plafondService.deletePlafond(id);
        return ResponseUtil.ok(null, "Successfully delete plafond");
    }

}
