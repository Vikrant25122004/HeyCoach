package com.HeyCoach.HeyCoach.Controllers;

import com.HeyCoach.HeyCoach.Services.mockinterview;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/mock")
public class MockInterview {
    @Autowired
    private mockinterview mockinterview;
    @PostMapping
    public ResponseEntity<?> mockinterview(@RequestParam("desc") String  desc){
        try {
            // Step 1: Get and sanitize HTML
            String rawHtml = mockinterview.mock(desc);
            if (rawHtml == null || rawHtml.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // Remove BOM and trim
            rawHtml = rawHtml.replace("\uFEFF", "").trim();

            // Debug: print first 100 characters
            rawHtml = rawHtml.replaceAll("(?s)```xml\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            System.out.println(rawHtml);

            // Step 2: Generate PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(rawHtml, null); // null = no base URI needed
            builder.toStream(outputStream);
            builder.run();

            // Step 3: Return PDF
            byte[] pdfData = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(pdfData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment().filename("tailored_resume.pdf").build());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // 🔍 this helps
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
