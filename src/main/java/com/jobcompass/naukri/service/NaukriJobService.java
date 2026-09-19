package com.jobcompass.naukri.service;

import com.jobcompass.naukri.config.util.NaukriHttpUtil;
import com.jobcompass.naukri.dto.NaukriJobResponse;
import com.jobcompass.naukri.dto.NaukriSearchResponse;
import com.jobcompass.naukri.dto.NaukriSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


import static com.jobcompass.naukri.constants.Uri.*;

@Service
public class NaukriJobService {

    NaukriSession naukriSession;
    NaukriLoginService naukriLoginService;
    RestClient naukriRestClient;
    NaukriHttpUtil naukriHttpUtil;


    private static final DateTimeFormatter SID_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

    private static final Logger log =
            LoggerFactory.getLogger(NaukriJobService.class);


    @Autowired
    public NaukriJobService(NaukriSession naukriSession,
                            NaukriLoginService naukriLoginService,
                            RestClient naukriRestClient,
                            NaukriHttpUtil naukriHttpUtil){
        this.naukriLoginService = naukriLoginService;
        this.naukriSession = naukriSession;
        this.naukriRestClient = naukriRestClient;
        this.naukriHttpUtil = naukriHttpUtil;

    }

    public List<NaukriJobResponse> searchJobs(
            String keyword,
            String location,
            int page,
            int jobAge,
            int experience,
            int resultsPerPage,
            String latLong
    ) {

        String seoKey = buildSeoKey(keyword, location, page);

        Map<String, Object> params = new LinkedHashMap<>();

        params.put("noOfResults", resultsPerPage);
        params.put("urlType", "search_by_keyword");
        params.put("searchType", "adv");
        params.put("keyword", keyword);
        params.put("k", keyword);
        params.put("pageNo", page);
        params.put("experience", experience);
        params.put("jobAge", jobAge);
        params.put("nignbevent_src", "jobsearchDeskGNB");
        params.put("seoKey", seoKey);
        params.put("src", "jobsearchDesk");
        params.put("latLong", latLong);


        UriComponentsBuilder uriBuilder =
                UriComponentsBuilder.fromUriString(JOB_SEARCH_URL);

        params.forEach(uriBuilder::queryParam);

        NaukriSearchResponse response = naukriRestClient.get()
                .uri(uriBuilder.build().encode().toUri())
                .headers(headers -> headers.addAll(searchHeaders()))
                .retrieve()
                .body(NaukriSearchResponse.class);

        return response.jobs();
    }



    private record SearchQuery(String keyword, String location) {}



    public List<NaukriJobResponse> fetchAllJobs() {

        List<SearchQuery> queries = List.of(
                new SearchQuery("Java backend developer", "Hyderabad"),
                new SearchQuery("Spring", ""),
                new SearchQuery("Java Developer", ""),
                new SearchQuery("Java developer", "Pune")
        );

        List<Integer> experienceLevels = List.of(4);
        int pages = 1;
        int jobAge = 2;

        // Deduplicate by ID while preserving the order jobs were found.
        Map<String, NaukriJobResponse> uniqueJobs = new LinkedHashMap<>();

        for (SearchQuery query : queries) {
            for (int experience : experienceLevels) {
                for (int page = 1; page <= pages; page++) {
                    try {
                        List<NaukriJobResponse> jobs = searchJobs(
                                query.keyword(),
                                query.location(),
                                page,
                                jobAge,
                                experience,
                                20,
                                ""


                        );

                        if (jobs == null || jobs.isEmpty()) {
                            break;
                        }

                        int previousSize = uniqueJobs.size();

                        for (NaukriJobResponse job : jobs) {
                            // Use your DTO's actual ID accessor.
                            String jobId = job.jobId();

                            if (jobId != null && !jobId.isBlank()) {
                                uniqueJobs.putIfAbsent(jobId, job);
                            }
                        }

                        log.info(
                                "Search keyword={}, location={}, exp={}, page={}: "
                                        + "fetched={}, new={}",
                                query.keyword(),
                                query.location(),
                                experience,
                                page,
                                jobs.size(),
                                uniqueJobs.size() - previousSize
                        );

                    } catch (RestClientException ex) {
                        log.warn(
                                "Search failed: keyword={}, location={}, exp={}, page={}: {}",
                                query.keyword(),
                                query.location(),
                                experience,
                                page,
                                ex.getMessage()
                        );

                        if (!pause(3000)) {
                            return new ArrayList<>(uniqueJobs.values());
                        }

                        continue;
                    }

                    if (!pause(1200)) {
                        return new ArrayList<>(uniqueJobs.values());
                    }
                }
            }
        }

        log.info("Total unique jobs collected: {}", uniqueJobs.size());
        return new ArrayList<>(uniqueJobs.values());
    }

    private boolean pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
            return true;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        }
    }


    public List<NaukriJobResponse> getRecommendedJobs() {

        String now = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());

        Map<String, String> clusterDates = Map.of(
                "apply", now,
                "preference", now,
                "profile", now,
                "similar_jobs", now
        );

        // HashMap allows the null clusterId.
        Map<String, Object> payload = new HashMap<>();
        payload.put("clusterId", null);
        payload.put("src", "recommClusterApi");
        payload.put("clusterSplitDate", clusterDates);

        NaukriSearchResponse response = naukriRestClient.post()
                .uri(RECOMMENDED_JOBS_URL)
                .headers(headers ->
                        headers.addAll(naukriHttpUtil.buildHeaders(true, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(NaukriSearchResponse.class);

        return response == null || response.jobs() == null
                ? List.of()
                : response.jobs();
    }




    public Map<String, Object> applyJob(String jobId,
                                        List<String> mandatorySkills,
                                        List<String> optionalSkills,
                                        String sid,
                                        String source) {
        if (jobId == null || jobId.isBlank()) {
            throw new IllegalArgumentException("jobId is required");
        }

        String effectiveSid = sid == null || sid.isBlank()
                ? SID_FORMAT.format(Instant.now()) + "0000000"
                : sid;

        // Matches the Python fallback for null or unknown sources.
        String applySrc = "search".equals(source) ? "srp" : "drecomm_apply";

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("strJobsarr", List.of(jobId));
        payload.put("logstr", "--" + applySrc + "-1-F-0-1--" + effectiveSid + "-");
        payload.put("flowtype", "show");
        payload.put("crossdomain", true);
        payload.put("jquery", 1);
        payload.put("rdxMsgId", "");
        payload.put("chatBotSDK", true);
        payload.put("mandatory_skills", mandatorySkills == null ? List.of() : mandatorySkills);
        payload.put("optional_skills", optionalSkills == null ? List.of() : optionalSkills);
        payload.put("applyTypeId", "107");
        payload.put("closebtn", "y");
        payload.put("applySrc", applySrc);
        payload.put("sid", effectiveSid);
        payload.put("mid", "");

        org.springframework.http.HttpHeaders httpHeaders = naukriHttpUtil.buildHeaders(true, Map.of(
                "appid", "121",
                "systemid", "jobseeker",
                "clientid", "d3skt0p",
                "accept", "application/json"
        ));

        Map<String, Object> response = naukriRestClient.post()
                .uri(APPLY_JOB_URL)
                .headers(h -> h.addAll(httpHeaders) )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                // Default RestClient handling preserves 4xx/5xx as exceptions.
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        if (response == null) {
            throw new IllegalStateException("Naukri returned an empty apply response");
        }
        return response;
    }


    private String buildSeoKey(String keyword, String location, int page) {

        String kwSlug = keyword
                .strip()
                .toLowerCase()
                .replace(".", "-dot-")
                .replace(" ", "-")
                .replace("+", "-")
                .replaceAll("^-+|-+$", "");

        if (location != null && !location.isBlank()) {

            String locSlug = location
                    .strip()
                    .toLowerCase()
                    .replace(" ", "-");

            return "%s-jobs-in-%s-%d"
                    .formatted(kwSlug, locSlug, page);
        }

        return "%s-jobs-%d"
                .formatted(kwSlug, page);
    }

    private HttpHeaders searchHeaders() {

        HttpHeaders headers = naukriHttpUtil.buildHeaders(false,null);

        headers.set("authority", "www.naukri.com");
        headers.set("accept", "application/json");
        headers.set("accept-encoding", "gzip, deflate");
        headers.set("accept-language", "en-US,en;q=0.9");
        headers.set("appid", "109");
        headers.set("gid", "LOCATION,INDUSTRY,EDUCATION,FAREA_ROLE");

        return headers;
    }


}
