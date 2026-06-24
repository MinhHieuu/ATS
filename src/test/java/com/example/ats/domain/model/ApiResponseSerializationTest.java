package com.example.ats.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializesMessageAndData() throws Exception {
        ApiResponse<List<String>> response = new ApiResponse<>("success", List.of("candidate"));

        String json = objectMapper.writeValueAsString(response);

        assertThat(json).isEqualTo("{\"message\":\"success\",\"data\":[\"candidate\"]}");
    }
}
