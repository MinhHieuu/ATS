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
        String candidateJson = mockMvc.perform(post("/api/candidates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Lan Nguyen",
                                  "email": "lan@example.com",
                                  "phone": "0901234567",
                                  "currentPosition": "Java Fresher",
                                  "yearsOfExperience": 0
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("lan@example.com"))
                .andReturn().getResponse().getContentAsString();

        String jobJson = mockMvc.perform(post("/api/jobs")
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
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn().getResponse().getContentAsString();

        JsonNode candidate = objectMapper.readTree(candidateJson);
        JsonNode job = objectMapper.readTree(jobJson);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "candidateId": %s,
                                  "jobId": %s,
                                  "source": "CAREER_SITE"
                                }
                                """.formatted(candidate.get("id").asText(), job.get("id").asText())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPLIED"));

        mockMvc.perform(get("/api/applications")
                        .param("candidateId", candidate.get("id").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobId").value(job.get("id").asLong()));
    }
}
