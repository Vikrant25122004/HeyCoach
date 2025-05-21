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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class tailorResumeService {
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


    public String tailor( String jobdesc) {
        HttpEntity<Map<String, Object>> httpEntity = buildRequestEntity(
                "Given the following job description: " + jobdesc + "\n\n" +
                        "Generate a complete, professional resume as raw **XHTML** (strictly well-formed XML using HTML5 structure). " +
                        "Ensure the output includes only XML (no Markdown, no code fences, no narrative). Do not use <pre>, <code>, or any extra wrapping elements.\n\n" +
                        "Required sections: Header (name, contact info), Summary, Skills (grouped by category), Experience (with job title, company, location, dates, and bullet points), Projects, and Education.\n\n" +
                        "Inside <head>, use a <style> block to define professional, elegant, modern styling. Ensure:\n" +
                        "- Clean, readable typography\n" +
                        "- Responsive layout using divs and sections\n" +
                        "- Padding and spacing for visual balance\n" +
                        "- Consistent colors and subtle borders\n" +
                        "- Section titles stand out clearly\n\n" +
                        "Strictly follow these constraints:\n" +
                        "- Use only well-formed XHTML/XML\n" +
                        "- All tags must be explicitly closed and properly nested\n" +
                        "- Escape all special characters, especially ampersands (& must be &amp;)\n" +
                        "- Avoid invalid characters and unescaped symbols\n" +
                        "- Do NOT include any explanations, only return pure XML starting from <!DOCTYPE html>\n\n" +
                        "This will be rendered on the server side using Spring Boot with OpenHTMLtoPDF. The output must not contain any syntax that causes XML or rendering errors.");
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
