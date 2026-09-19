package com.jobcompass.naukri.controller;


import com.jobcompass.naukri.agent.AutomatedWorkflowService;
import com.jobcompass.naukri.dto.NaukriJobResponse;
import com.jobcompass.naukri.service.NaukriJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auto")
public class AutomatedWorkflow {

    @Autowired
    NaukriJobService naukriJobService;

    @GetMapping("/fetchAllJobs")
    public ResponseEntity<List<NaukriJobResponse>> fetchAllJobs(){
        return ResponseEntity.ok(naukriJobService.fetchAllJobs());

    }
}
