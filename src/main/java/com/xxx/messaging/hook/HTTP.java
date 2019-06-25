package com.xxx.messaging.hook;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
public class HTTP extends AbstractHook {
    private RestTemplate restTemplate;
    private String url;
    private String method;
    private Map<String, String> headers;

    @Builder
    HTTP(RestTemplate restTemplate, String url, String method, Map<String, String> headers, ReturnCode onSuccess, ReturnCode onFailure) {
        super(onSuccess, onFailure);
        this.restTemplate = restTemplate;
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    @Override
    @Retryable
    public boolean call(String id, String message) {
        HttpEntity<String> entity = new HttpEntity<>(message, httpHeaders());

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.resolve(method), entity, String.class);
        return result.getStatusCode().is2xxSuccessful();
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (this.headers != null) {
            this.headers.forEach(headers::add);
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.noCache());

        return headers;
    }
}