package com.jobcompass.naukri.controller;

import com.jobcompass.naukri.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    OpenAiService openAiService;

    @PostMapping("/test")
    public String testLLM(String prompt){
        return openAiService.ask(prompt);
    }
}
