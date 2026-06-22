package com.example.ats.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "ats.upload")
public record UploadProperties(Path directory, String publicPath) {
}
