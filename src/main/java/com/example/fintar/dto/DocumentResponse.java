package com.example.fintar.dto;

import com.example.fintar.enums.DocType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private UUID id;
    private String filename;
    private DocType docType;
    private String contentType;
    private Long size;
}
