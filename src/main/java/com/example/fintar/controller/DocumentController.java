package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.DocumentResponse;
import com.example.fintar.enums.DocType;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.service.CustomerDetailService;
import com.example.fintar.service.DocumentService;
import com.example.fintar.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;
  private final CustomerDetailService customerDetailService;

  @PostMapping("/{type}")
  public ResponseEntity<ApiResponse<DocumentResponse>> uploadLoanDoc(
      @PathVariable String type, @RequestParam("file") MultipartFile file) {
    DocType docType;
    try {
      docType = DocType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new BusinessValidationException("No such type exist");
    }

    DocumentResponse documentResponse = documentService.uploadDocument(file, docType);
    return ResponseUtil.ok(documentResponse, "Successfully upload file : " + type);
  }
}
