package com.HeyCoach.HeyCoach.Services;

import com.HeyCoach.HeyCoach.Entity.ExistingJobs;
import com.HeyCoach.HeyCoach.Entity.Jobs;
import com.HeyCoach.HeyCoach.Repository.JobsTrendingRepo;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExistingJobsService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JobsTrendingRepo jobsTrendingRepo;

    public List<Map<String, Object>> getJobs(Jobs jobs) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        HashMap<String,Object> title= new HashMap<>();
        title.put("input" , jobs);

        HttpEntity<HashMap> request = new HttpEntity<>(title, headers);

        // URL with token in query params
        String url = "https://api.apify.com/v2/acts/bebity~linkedin-jobs-scraper/run-sync-get-dataset-items?token=apify_api_mYkFDZdQIRsDPlBFmYVQb6WhuhQSbe3l4lEi";

        // Make the POST request and expect a List of results (JSON array)
        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                List.class
        );

        return response.getBody(); // List of job results as Map<String, Object>
    }
    public void savejobs(List<Map<String, Object>> jobs){
        for (Map<String,Object> map : jobs){
            ExistingJobs existingJobs = new ExistingJobs();
            if (map.containsKey("applicationCount")){
                existingJobs.setApplicationCount((String) map.get("applicationCount"));
            }
            if (map.containsKey("companyName")){
                existingJobs.setCompanyName((String) map.get("companyName"));

            }
            if (map.containsKey("contractType")){
                existingJobs.setContractType((String) map.get("contractType"));
            }
            if (map.containsKey("description")){
                existingJobs.setDescription((String) map.get("description"));
            }
            if (map.containsKey("experienceLevel")){
                existingJobs.setExperienceLevel((String) map.get("experienceLevel"));
            }
            if (map.containsKey("salary")){
                existingJobs.setSalary((String) map.get("salary"));
            }
            System.out.println(existingJobs);
            jobsTrendingRepo.save(existingJobs);

        }

    }
}
