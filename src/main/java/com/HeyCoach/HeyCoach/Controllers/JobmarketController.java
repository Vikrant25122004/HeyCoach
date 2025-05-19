package com.HeyCoach.HeyCoach.Controllers;

import com.HeyCoach.HeyCoach.Services.JobMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobmarket")
public class JobmarketController {
    @Autowired
   private JobMarketService jobMarketService;
    @GetMapping
    public ResponseEntity<?> jobmarket(){
        String response = jobMarketService.Jobmarkettrend();
        System.out.print(response);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
