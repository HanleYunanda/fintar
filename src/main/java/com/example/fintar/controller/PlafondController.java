package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.CreatePlafondRequest;
import com.example.fintar.entity.Plafond;
import com.example.fintar.service.PlafondService;
import com.example.fintar.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plafonds")
public class PlafondController {

    @Autowired
    private PlafondService plafondService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Plafond>>> index() {
        List<Plafond> plafonds = plafondService.getAllPlafond();
        return ResponseUtil.ok(null, "Successfully get all plafonds");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Plafond>> create(
            @RequestBody @Valid CreatePlafondRequest req
    ) {
        Plafond createdPlafond = plafondService.createPlafond(req);
        return ResponseUtil.ok(createdPlafond, "Successfully create new user");
    }

}
