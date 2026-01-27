package com.example.fintar.controller;

import com.example.fintar.base.ApiResponse;
import com.example.fintar.dto.DocumentResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.enums.DocType;
import com.example.fintar.exception.BusinessValidationException;
import com.example.fintar.service.CustomerDetailService;
import com.example.fintar.service.DocumentService;
import com.example.fintar.util.ResponseUtil;
import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;
  private final CustomerDetailService customerDetailService;

  @PostMapping("/{type}")
  @PreAuthorize("hasAuthority('UPLOAD_DOCUMENT')")
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

  @GetMapping("/{id}")
  public ResponseEntity<UrlResource> show(
          @PathVariable UUID id
          ) throws Exception {
    Document document = documentService.getDocumentEntityById(id);
    UrlResource resource = documentService.getDocumentFile(document);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFileName() + "\"")
            .contentType(MediaTypeFactory.getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM))
            .body(resource);
  }

  @GetMapping("/customer-detail/{customerDetailId}")
  public ResponseEntity<ApiResponse<List<DocumentResponse>>> allByCustomerDetail(
          @PathVariable UUID customerDetailId
  ) {
    CustomerDetail customerDetail = customerDetailService.getCustomerDetailEntityById(customerDetailId);
    List<DocumentResponse> documentResponses = customerDetail.getDocuments()
            .stream()
            .map(document -> DocumentResponse.builder()
                    .id(document.getId())
                    .filename(document.getFileName())
                    .fileUri(document.getFileUri())
                    .docType(document.getDocType())
                    .contentType(document.getContentType())
                    .size(document.getSize())
                    .build())
            .toList();
    return ResponseUtil.ok(documentResponses, "Successfully get documents");
  }
}
