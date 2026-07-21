package com.example.ats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationFlowIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void candidateCanApplyToAnOpenJob() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis() % 1_000_000_000);
        String email = "lan" + suffix + "@example.com";
        String phone = "09" + suffix;

        String candidateJson = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Lan Nguyen",
                                  "email": "%s",
                                  "phone": "%s",
                                  "password": "password123",
                                  "currentPosition": "Java Fresher",
                                  "yearsOfExperience": 0
                                }
                                """.formatted(email, phone)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(email))
                .andReturn().getResponse().getContentAsString();

        String loginJson = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "password123"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isString())
                .andReturn().getResponse().getContentAsString();

        String accessToken = objectMapper.readTree(loginJson).get("data").get("accessToken").asText();

        String jobJson = mockMvc.perform(post("/api/jobs")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Junior Java Developer",
                                  "location": "Ho Chi Minh City",
                                  "description": "Build and maintain Spring Boot services",
                                  "requirements": "Java 17 and Spring Boot",
                                  "employmentType": "FULL_TIME",
                                  "salaryMin": 1000,
                                  "salaryMax": 1500
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("OPEN"))
                .andReturn().getResponse().getContentAsString();

        JsonNode candidate = objectMapper.readTree(candidateJson).get("data");
        JsonNode job = objectMapper.readTree(jobJson).get("data");

        mockMvc.perform(post("/api/applications")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "candidateId": %s,
                                  "jobId": %s,
                                  "source": "CAREER_SITE"
                                }
                                """.formatted(candidate.get("id").asText(), job.get("id").asText())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("APPLICATION_CREATED"))
                .andExpect(jsonPath("$.data.source").value("CAREER_SITE"));

        mockMvc.perform(get("/api/applications")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("candidateId", candidate.get("id").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].jobId").value(job.get("id").asLong()));
    }
}
