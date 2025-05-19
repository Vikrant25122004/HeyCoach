package com.HeyCoach.HeyCoach.Controllers;

import com.HeyCoach.HeyCoach.Entity.ExistingJobs;
import com.HeyCoach.HeyCoach.Entity.Jobs;
import com.HeyCoach.HeyCoach.Services.ExistingJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ExistingJobsController {
    @Autowired
    private ExistingJobsService existingJobsService;
    @PostMapping("/getallJobs")
    public ResponseEntity<?> getExistingJobs(@RequestBody Jobs jobs){
        try {


            List<Map<String, Object>> existingJobs = existingJobsService.getJobs(jobs);
            existingJobsService.savejobs(existingJobs);
            return new ResponseEntity<>(existingJobs, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
