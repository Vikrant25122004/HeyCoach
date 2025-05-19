package com.HeyCoach.HeyCoach.Services;

import com.HeyCoach.HeyCoach.Entity.ExistingJobs;
import com.HeyCoach.HeyCoach.Repository.JobsTrendingRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;

import org.apache.coyote.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class JobMarketService {
    @Autowired
    private JobsTrendingRepo jobsTrendingRepo;

    @Autowired
    private RestTemplate restTemplate;
    public static HttpEntity<Map<String, Object>> buildRequestEntity(String prompt) {
        // Inner "text" map
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("text", prompt);

        // parts list
        List<Map<String, Object>> partsList = new ArrayList<>();
        partsList.add(textMap);

        // Add "role": "user" here
        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("parts", partsList);

        // Full request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", Collections.singletonList(messageMap));

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("Payload: " + new Gson().toJson(requestBody)); // Debug payload


        return new HttpEntity<>(requestBody, headers);
    }


    public String Jobmarkettrend() {
        List<ExistingJobs> jobs = jobsTrendingRepo.findAll();

        Gson gson = new Gson();
        String jsonString = gson.toJson(jobs);

        HttpEntity<Map<String, Object>> httpEntity = buildRequestEntity(
                jsonString +
                        "Analyze all the data and give me detailed analysis of Job market trends in line chart or pie chart with useful insights"
        );

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAQEV5lsMgF7S-5AuHsGK4xowkBaVhvbTY";

        try {
            // Get full HTTP response as string
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, httpEntity, String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("Raw response: " + responseBody); // Debug

            if (responseBody == null || responseBody.isEmpty()) {
                return "Empty response from Gemini.";
            }

            // Parse raw JSON string to JsonObject
            JsonObject response = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray candidates = response.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                JsonObject content = firstCandidate.getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && parts.size() > 0) {
                    JsonObject firstPart = parts.get(0).getAsJsonObject();
                    return firstPart.get("text").getAsString();
                }
            }
            return "No valid content in Gemini response.";

        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Error Body: " + e.getResponseBodyAsString());
            return "Error from Gemini API: " + e.getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error occurred.";
        }
    }
}
