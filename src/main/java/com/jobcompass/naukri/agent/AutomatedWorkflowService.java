package com.jobcompass.naukri.agent;

import com.jobcompass.naukri.dto.NaukriJobResponse;
import com.jobcompass.naukri.service.NaukriJobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AutomatedWorkflowService {

    private static final Logger log =
            LoggerFactory.getLogger(AutomatedWorkflowService.class);



    @Autowired
    AutomatedWorkflowService(NaukriJobService naukriJobService){
        this.naukriJobService = naukriJobService;
    }

}
