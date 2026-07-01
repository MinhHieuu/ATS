package com.example.ats.application.service;

import com.example.ats.infrastructure.config.UploadProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDirectory;
    private final String publicPath;

    public FileStorageService(UploadProperties properties) {
        this.uploadDirectory = properties.directory().toAbsolutePath().normalize();
        this.publicPath = normalizePublicPath(properties.publicPath());
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            return "";
        }

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()
        );
        String extension = getExtension(originalName);
        String storedName = UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(storedName).normalize();

        if (!destination.getParent().equals(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid file name");
        }

        try {
            Files.createDirectories(uploadDirectory);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new FileStorageException("Could not store uploaded file", exception);
        }

        return storedName;
    }

    private String getExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf('.');
        return extensionIndex >= 0 ? fileName.substring(extensionIndex).toLowerCase() : "";
    }

    private String normalizePublicPath(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/")
                ? normalized.substring(0, normalized.length() - 1)
                : normalized;
    }

    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
