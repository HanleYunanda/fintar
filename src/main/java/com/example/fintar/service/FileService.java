package com.example.fintar.service;

import com.example.fintar.config.FileStorageProperties;
import com.example.fintar.dto.FileResponse;
import com.example.fintar.enums.DocType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootPath;

    public FileService(FileStorageProperties properties) {
        this.rootPath = Paths.get(properties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(this.rootPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public FileResponse upload(MultipartFile file, String path, String storedFilename) {
        try {
            Path dir = rootPath.resolve(path);
            Files.createDirectories(dir);

            Path target = dir.resolve(storedFilename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return new FileResponse(
                    storedFilename,
                    file.getOriginalFilename(),
                    "/files/" + path + "/" + storedFilename,
                    file.getContentType(),
                    file.getSize()
            );

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    public String getExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name != null && name.contains(".")
                ? name.substring(name.lastIndexOf("."))
                : "";
    }

}
