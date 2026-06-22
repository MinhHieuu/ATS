package com.example.ats.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(UploadProperties.class)
public class UploadConfig implements WebMvcConfigurer {
    private final UploadProperties properties;

    public UploadConfig(UploadProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String publicPath = normalizePublicPath(properties.publicPath());
        String location = properties.directory().toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler(publicPath + "/**")
                .addResourceLocations(location);
    }

    private String normalizePublicPath(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return normalized.endsWith("/")
                ? normalized.substring(0, normalized.length() - 1)
                : normalized;
    }
}
