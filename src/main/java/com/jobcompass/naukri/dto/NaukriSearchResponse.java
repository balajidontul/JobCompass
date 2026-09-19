package com.jobcompass.naukri.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaukriSearchResponse(

        @JsonAlias({"jobDetails", "jobs"})
        List<NaukriJobResponse> jobs

) {
    public NaukriSearchResponse {
        jobs = jobs == null ? List.of() : jobs;
    }
}
