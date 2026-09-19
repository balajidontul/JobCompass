package com.jobcompass.naukri.config.util;

import com.jobcompass.naukri.dto.NaukriSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaukriHttpUtil {

    @Autowired
    public NaukriHttpUtil(NaukriSession naukriSession) {
        this.naukriSession = naukriSession;
    }

    private final NaukriSession naukriSession;

    private static final Map<String, String> DEFAULT_HEADERS = Map.of(
            "accept", "application/json",
            "appid", "105",
            "clientid", "d3skt0p",
            "content-type", "application/json",
            "referer", "https://www.naukri.com/nlogin/login",
            "systemid", "jobseeker",
            "x-requested-with", "XMLHttpRequest"
    );



    public HttpHeaders buildHeaders(
            boolean auth,
            Map<String, String> extra
    ) {

        HttpHeaders headers = new HttpHeaders();

        DEFAULT_HEADERS.forEach(headers::set);

        if (auth && naukriSession.getBearerToken() != null) {
            headers.setBearerAuth(naukriSession.getBearerToken());
            headers.set("systemid", "Naukri");
        }

        if (extra != null) {
            extra.forEach(headers::set);
        }

        return headers;
    }
}
