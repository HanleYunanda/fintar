package com.example.fintar.service;

import com.example.fintar.dto.DocumentResponse;
import com.example.fintar.dto.FileResponse;
import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.entity.Document;
import com.example.fintar.enums.DocType;
import com.example.fintar.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final FileService fileService;
    private final CustomerDetailService customerDetailService;
    private final DocumentRepository documentRepository;

    @Transactional
    public DocumentResponse uploadDocument(
            MultipartFile file,
            DocType docType
    ) {
        // Get customer detail
        CustomerDetail customerDetail = customerDetailService.getCustomerDetailEntityByLoggedInUser();

        // Create metadata
        Document document = Document.builder()
                .docType(docType)
                .contentType(file.getContentType())
                .size(file.getSize())
                .customerDetail(customerDetail)
                .build();
        document = documentRepository.saveAndFlush(document);

        // Get Id
        UUID documentId = document.getId();

        // Create rename file
        String extension = fileService.getExtension(file);
        String storedFilename = docType + "_" + documentId + extension;

        // Upload file
        FileResponse response = fileService.upload(
                file,
                customerDetail.getId().toString(),
                storedFilename
        );

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

//    @Transactional
//    public void softDeletePrevDocument(CustomerDetail customerDetail, DocType docType) {
//        Optional<Document> documentOpt = documentRepository.findByCustomerDetailAndDocType(customerDetail, docType);
//        if(documentOpt.isPresent()) {
//            Document document = documentOpt.get();
//            document.setIsDeleted(true);
//            documentRepository.save(document);
//        }
//    }

}
