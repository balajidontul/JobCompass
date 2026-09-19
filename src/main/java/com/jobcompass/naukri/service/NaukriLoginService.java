package com.jobcompass.naukri.service;

import com.jobcompass.naukri.config.util.NaukriHttpUtil;
import com.jobcompass.naukri.dto.LoginRequest;
import com.jobcompass.naukri.dto.NaukriSession;
import com.jobcompass.naukri.exception.LoginFailedException;
import com.jobcompass.naukri.exception.LoginRequiredException;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.jobcompass.naukri.constants.Uri.LOGIN_URL;

@Service
public class NaukriLoginService {

    @Value("${naukri.username}")
    private String username;

    @Value("${naukri.password}")
    private String password;

    @Qualifier("naukriRestClient")
    RestClient naukriRestClient;

    BasicCookieStore cookieStore;

    NaukriSession naukriSession;

    NaukriHttpUtil naukriHttpUtil;

    @Autowired
    public NaukriLoginService(RestClient naukriRestClient,
                              BasicCookieStore cookieStore,
                              NaukriSession naukriSession,
                              NaukriHttpUtil naukriHttpUtil){
        this.naukriSession = naukriSession;
        this.cookieStore = cookieStore;
        this.naukriRestClient = naukriRestClient;
        this.naukriHttpUtil = naukriHttpUtil;

    }


    public String doLogin() throws LoginFailedException, LoginRequiredException {
        ResponseEntity<String> response = loginRequest();

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new LoginFailedException(response.getBody());
        }

        String token = cookieStore.getCookies().stream()
                .filter(cookie ->
                        "nauk_at".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() ->
                        new LoginRequiredException("No token"));

        naukriSession.setBearerToken(token);
        naukriSession.setCookies(cookieStore);

        return token;
    }

    private ResponseEntity<String> loginRequest() throws LoginRequiredException {
        HttpHeaders httpHeaders = naukriHttpUtil.buildHeaders(false,null);
        return naukriRestClient.post()
                .uri(LOGIN_URL)
                .headers( h -> h.addAll(httpHeaders))
                .body(new LoginRequest(username, password))
                .retrieve()
                .toEntity(String.class);
    }




}
