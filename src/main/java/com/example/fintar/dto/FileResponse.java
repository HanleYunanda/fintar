package com.example.fintar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
  private String filename;
  private String originalFilename;
  private String uri;
  private String contentType;
  private Long size;
}
