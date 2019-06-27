package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.HookFailedException;
import com.xxx.messaging.Status;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
public class HTTP extends Hook {
    private RestTemplate restTemplate;
    private String url;
    private String method;
    private Map<String, String> headers;

    @Builder
    HTTP(RestTemplate restTemplate, String url, String method, Map<String, String> headers, Status onError) {
        super(onError);
        this.restTemplate = restTemplate;
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    @Override
    public Status call(String message) throws HookFailedException {
        HttpEntity<String> entity = new HttpEntity<>(message, httpHeaders());

        ResponseEntity<Response> result = exchange(entity);
        HttpStatus statusCode = result.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new HookFailedException("HTTP hook failed: " + statusCode.getReasonPhrase());
        }

        Status status = result.getBody().getStatus();
        if (status == null) {
            throw new HookFailedException("HTTP hook failed: null status");
        }

        return status;
    }

    @Retryable
    private ResponseEntity<Response> exchange(HttpEntity<String> entity) {
        return restTemplate.exchange(url, HttpMethod.resolve(method), entity, Response.class);
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

    @Data
    private static class Response {
        private Status status;
    }
}