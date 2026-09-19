package com.jobcompass.naukri.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaukriJobResponse(
        String jobId,
        String title,
        String companyName,
        String location,
        String RequiredExperience,
        String offeredSalary,
        String createdDate,
        String jdURL,
        String jobDescription,
        List<String> tag
) {
        public List<String> tags() {
                return tag;
        }
}