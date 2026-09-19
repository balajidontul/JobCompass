package com.jobcompass.naukri.dto;

import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class NaukriSession {

    private String bearerToken;
    private CookieStore cookies;


    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public CookieStore getCookies() {
        return cookies;
    }

    public void setCookies(CookieStore cookies) {
        this.cookies = cookies;
    }




}
