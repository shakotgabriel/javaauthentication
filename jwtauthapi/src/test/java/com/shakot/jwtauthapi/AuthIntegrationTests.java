package com.shakot.jwtauthapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class AuthIntegrationTests {

        private static final Pattern TOKEN_PATTERN = Pattern.compile("\\\"token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerAndAccessUserEndpointShouldSucceed() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@example.com";

        String registerBody = """
                {
                  "name": "Demo User",
                  "email": "%s",
                  "password": "Password123"
                }
                """.formatted(email);

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andReturn();

        String token = extractToken(registerResult.getResponse().getContentAsString());
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Register response did not include token");
        }
    }

    @Test
    void loginShouldReturnTokenForExistingUser() throws Exception {
        String email = "login-" + UUID.randomUUID() + "@example.com";

        String registerBody = """
                {
                  "name": "Login User",
                  "email": "%s",
                  "password": "Password123"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {
                  "email": "%s",
                  "password": "Password123"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void loginShouldFailForBadCredentials() throws Exception {
        String loginBody = """
                {
                  "email": "unknown@example.com",
                  "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized());
    }

        private String extractToken(String json) {
                Matcher matcher = TOKEN_PATTERN.matcher(json);
                if (!matcher.find()) {
                        throw new IllegalStateException("Token not present in response: " + json);
                }
                return matcher.group(1);
        }
}
