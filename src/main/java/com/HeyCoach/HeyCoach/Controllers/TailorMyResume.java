package com.HeyCoach.HeyCoach.Controllers;

import com.HeyCoach.HeyCoach.Services.tailorResumeService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/Tailor-Resume")
public class TailorMyResume {

    @Autowired
    private tailorResumeService tailorResumeService;

    @PostMapping
    public ResponseEntity<ByteArrayResource> tailorResume(@RequestParam("description") String jobdesc) {
        try {
            // Step 1: Get and sanitize HTML
            String rawHtml = tailorResumeService.tailor(jobdesc);
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
