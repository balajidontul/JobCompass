package com.jobcompass.naukri.controller;

import com.jobcompass.naukri.exception.LoginFailedException;
import com.jobcompass.naukri.exception.LoginRequiredException;
import com.jobcompass.naukri.service.NaukriLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/naukri")
public class AuthController {

    @Autowired
    NaukriLoginService naukriLoginService;

    @GetMapping("/login")
    public String doNaukriLogin() throws LoginRequiredException, LoginFailedException {
        return naukriLoginService.doLogin();
    }
}
