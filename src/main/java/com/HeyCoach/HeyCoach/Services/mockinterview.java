package com.HeyCoach.HeyCoach.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class mockinterview {
    @Autowired
    private RestTemplate restTemplate;
    String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAQEV5lsMgF7S-5AuHsGK4xowkBaVhvbTY";

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


    public String mock( String jobdesc) {
        HttpEntity<Map<String, Object>> httpEntity = buildRequestEntity(
                "Given the following job description:" + jobdesc  +
                        "\n" +
                        "Generate a complete interview Q&A document in pure XML format.\n" +
                        "\n" +
                        "Requirements:\n" +
                        "- All output must be well-formed and valid XML (not HTML/XHTML).\n" +
                        "- Use <interview> as the root element.\n" +
                        "- Each Q&A pair must be enclosed in a <section>.\n" +
                        "- Use inline styles within <question> and <answer> elements. Do NOT use a <style> block.\n" +
                        "- Styles to apply inline:\n" +
                        "    - <question style=\"font-family: Arial, sans-serif; font-weight: bold; color: #336699; margin-bottom: 5px;\">\n" +
                        "    - <answer style=\"font-family: Arial, sans-serif; color: #666666; margin-bottom: 15px;\">\n" +
                        "- Ensure special characters are properly escaped (e.g., & becomes &amp;).\n" +
                        "- Do NOT include any code fences, markdown, or narrative — only pure XML should be returned.\n" +
                        "\n" +
                        "Example structure:\n" +
                        "\n" +
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<interview>\n" +
                        "  <section>\n" +
                        "    <question style=\"font-family: Arial, sans-serif; font-weight: bold; color: #336699; margin-bottom: 5px;\">\n" +
                        "      What are the key features of Java 17 you have used?\n" +
                        "    </question>\n" +
                        "    <answer style=\"font-family: Arial, sans-serif; color: #666666; margin-bottom: 15px;\">\n" +
                        "      I have used features like sealed classes and records in Java 17 to enhance code clarity and reduce boilerplate...\n" +
                        "    </answer>\n" +
                        "  </section>\n" +
                        "  <!-- Repeat <section> for each Q&A -->\n" +
                        "</interview>\n"



);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, httpEntity, String.class);
            String responseBody = responseEntity.getBody();
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


        }
        catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Error Body: " + e.getResponseBodyAsString());
            return "Error from Gemini API: " + e.getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error occurred.";
        }
    }
}
