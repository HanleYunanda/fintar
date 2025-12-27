package com.example.fintar.service;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.PlafondRequest;
import com.example.fintar.dto.PlafondResponse;
import com.example.fintar.entity.Plafond;
import com.example.fintar.entity.User;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.mapper.PlafondMapper;
import com.example.fintar.repository.PlafondRepository;
import com.example.fintar.util.ResponseUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlafondService {

    @Autowired
    private PlafondRepository plafondRepository;

    @Autowired
    private PlafondMapper plafondMapper;

    @Cacheable(value = "plafonds")
    public List<PlafondResponse> getAllPlafond() {
        return plafondMapper.toResponseList(plafondRepository.findAll());
    }

    @Transactional
    @CacheEvict(value = "plafonds", allEntries = true)
    public PlafondResponse createPlafond(PlafondRequest req) {

        Plafond plafond = plafondMapper.fromRequest(req);
        return plafondMapper.toResponse(plafondRepository.save(plafond));
    }

    public Plafond getPlafondEntity(UUID id) {
        Optional<Plafond> plafond = plafondRepository.findById(id);
        if(plafond.isEmpty()) throw new ResourceNotFoundException("Plafond with id " + id + " not found");
        return plafond.get();
    }

    public PlafondResponse getPlafond(UUID id) {
        return plafondMapper.toResponse(this.getPlafondEntity(id));
    }

    @Transactional
    @CacheEvict(value = "plafonds", allEntries = true)
    public PlafondResponse updatePlafond(UUID id, PlafondRequest req) {
        Plafond plafond = this.getPlafondEntity(id);
        plafond.setName(req.getName());
        plafond.setMaxAmount(req.getMaxAmount());
        plafond.setMaxTenor(req.getMaxTenor());
        return plafondMapper.toResponse(plafondRepository.save(plafond));
    }

    @Transactional
    @CacheEvict(value = "plafonds", allEntries = true)
    public void deletePlafond(UUID id) {
        Plafond plafond = this.getPlafondEntity(id);
        plafondRepository.delete(plafond);
    }
}
