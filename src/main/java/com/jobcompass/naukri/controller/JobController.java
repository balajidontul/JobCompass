package com.jobcompass.naukri.controller;

import com.jobcompass.naukri.dto.NaukriJobResponse;
import com.jobcompass.naukri.service.NaukriJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    NaukriJobService naukriJobService;

    @GetMapping("/search")
    public ResponseEntity<List<NaukriJobResponse>> searchJob(){
        List<NaukriJobResponse> naukriJobResponsesList= naukriJobService.searchJobs("Java Developer", "Pune",
                2, 1, 4, 20,"" );

        return ResponseEntity.ok(naukriJobResponsesList);
    }

    @GetMapping("/recomm")
    public ResponseEntity<List<NaukriJobResponse>> getRecommJobs(){
        List<NaukriJobResponse> naukriJobResponsesList= naukriJobService.getRecommendedJobs();

        return ResponseEntity.ok(naukriJobResponsesList);
    }
}
