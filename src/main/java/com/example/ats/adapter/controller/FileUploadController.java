package com.example.ats.adapter.controller;

import com.example.ats.application.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public UploadResponse upload(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        FileStorageService.StoredFile storedFile = fileStorageService.store(file);
        String fileUrl = request.getRequestURL()
                .toString()
                .replace(request.getRequestURI(), storedFile.url());

        return new UploadResponse(
                storedFile.originalName(),
                storedFile.storedName(),
                fileUrl,
                storedFile.size()
        );
    }

    public record UploadResponse(
            String originalName,
            String storedName,
            String url,
            long size
    ) {
    }
}
