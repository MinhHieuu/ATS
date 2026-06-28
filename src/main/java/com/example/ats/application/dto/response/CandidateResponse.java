package com.example.ats.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse{
        private Long id;
        private UserResponse user;
        private String linkedinUrl;
        private String githubUrl;
        private String portfolioUrl;
        private String currentPosition;
        private Integer yearsOfExperience;
        private Instant createdAt;
        private Instant updatedAt;

        public String getEmail() {
                return user == null ? null : user.getEmail();
        }

        public String getFullname() {
                return user == null ? null : user.getFullname();
        }

        public String getPhone() {
                return user == null ? null : user.getPhone();
        }
}
