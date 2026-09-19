package com.jobcompass.naukri.dto;

import org.springframework.beans.factory.annotation.Value;

public record LoginRequest(
        String username,
        String password
) {}
