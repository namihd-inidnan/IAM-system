package com.example.demo_app_backend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IamService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String IAM_URL = "http://localhost:8080";

    // 🔹 ENABLE USER
    public void enableUser(String email, String token) {

        String url = IAM_URL + "/admin/enable-user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token); // 🔥 pass JWT

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(Map.of("targetEmail", email), headers);

        restTemplate.postForObject(url, request, String.class);
    }

    // 🔹 DISABLE USER
    public void disableUser(String email, String token) {

        String url = IAM_URL + "/admin/disable-user";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(Map.of("targetEmail", email), headers);

        restTemplate.postForObject(url, request, String.class);
    }

    // 🔹 ASSIGN ROLE
    public void assignRole(String email, String role, String token) {

        String url = IAM_URL + "/admin/assign-role";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Map<String, String>> request =
                new HttpEntity<>(
                        Map.of(
                                "targetEmail", email,
                                "roleName", role
                        ),
                        headers
                );

        restTemplate.postForObject(url, request, String.class);
    }
}