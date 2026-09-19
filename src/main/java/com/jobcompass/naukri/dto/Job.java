package com.jobcompass.naukri.dto;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record Job(
        String jobId,
        String title,
        String company,
        String location,
        String experience,
        String salary,
        Instant postedAt,
        URI applyLink,
        String description,
        List<String> tags
) {
    public Job {
        tags = tags == null
                ? List.of()
                : List.copyOf(tags);

        description = description == null
                ? ""
                : description;
    }
}