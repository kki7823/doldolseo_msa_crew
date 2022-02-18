package com.doldolseo.doldolseo_msa_crew.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OtherRestUtil {
    private final RestTemplate restTemplate = new RestTemplate();

    public void member_UpdateRole(String url, String authHeader, String id, String action) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Accept", "application/json");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("id", id)
                .queryParam("action", action);

        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);
    }
}
