package com.example.fintar.service;

import com.example.fintar.dto.DocumentResponse;
import com.example.fintar.dto.FileResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.enums.DocType;
import com.example.fintar.exception.ResourceNotFoundException;
import com.example.fintar.repository.DocumentRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

  private final FileService fileService;
  private final CustomerDetailService customerDetailService;
  private final DocumentRepository documentRepository;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Transactional
  public DocumentResponse uploadDocument(MultipartFile file, DocType docType) {
    // Get customer detail
    CustomerDetail customerDetail = customerDetailService.getCustomerDetailEntityByLoggedInUser();

    // Inactivate prev metadata
    this.inactivatePrevDocument(customerDetail, docType);

    // Create metadata
    Document document =
        Document.builder()
            .docType(docType)
            .contentType(file.getContentType())
            .size(file.getSize())
            .customerDetail(customerDetail)
            .isActive(true)
            .build();
    document = documentRepository.saveAndFlush(document);

    // Get Id
    UUID documentId = document.getId();

    // Create rename file
    String extension = fileService.getExtension(file);
    String storedFilename = docType + "_" + documentId + extension;

    // Upload file
    FileResponse response =
        fileService.upload(file, customerDetail.getId().toString(), storedFilename);

    // Update metadata
    document.setFileName(storedFilename);
    document.setFileUri(response.getUri());

    document = documentRepository.save(document);

    return DocumentResponse.builder()
        .id(document.getId())
        .filename(document.getFileName())
        .contentType(document.getContentType())
        .size(document.getSize())
        .docType(document.getDocType())
        .build();
  }

  @Transactional
  public void inactivatePrevDocument(CustomerDetail customerDetail, DocType docType) {
    Optional<Document> documentOpt =
        documentRepository.findByCustomerDetailAndDocTypeAndIsActiveTrue(customerDetail, docType);
    if (documentOpt.isPresent()) {
      Document document = documentOpt.get();
      document.setIsActive(false);
      documentRepository.save(document);
    }
  }

  public List<Document> getDocumentEntitiesByCustomerDetail(CustomerDetail customerDetail) {
    return documentRepository.findByCustomerDetailAndIsActiveTrue(customerDetail);
  }

  public Document getDocumentEntityById(UUID id) {
    Optional<Document> document = documentRepository.findById(id);
    if (document.isEmpty())
      throw new ResourceNotFoundException("Document with id " + id + " not found");
    return document.get();
  }

  public UrlResource getDocumentFile(Document document) throws Exception {
    Path baseDir = Paths.get(System.getProperty("user.dir"));
    Path path = baseDir.resolve(document.getFileUri()).normalize();
    UrlResource resource = new UrlResource(path.toUri());

    System.out.println(path);
    System.out.println(resource);
    if (!resource.exists()) {
      throw new ResourceNotFoundException("File not found");
    }
    return resource;
  }
}
