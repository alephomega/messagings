package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.HookFailedException;
import com.xxx.messaging.Status;
import com.xxx.messaging.Symbol;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
@Symbol("HTTP")
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
    public Hook.Response call(String message) throws HookFailedException {
        HttpEntity<String> entity = new HttpEntity<>(message, httpHeaders());

        ResponseEntity<Hook.Response> result = exchange(entity);
        HttpStatus statusCode = result.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new HookFailedException("HTTP hook failed: " + statusCode.getReasonPhrase());
        }

        Response body = result.getBody();
        if (body == null) {
            throw new HookFailedException("HTTP hook failed: Response body cannot be null");
        }

        Status status = body.getStatus();
        if (status == null) {
            throw new HookFailedException("HTTP hook failed: Response status cannot be null");
        }

        return body;
    }

    @Retryable
    private ResponseEntity<Hook.Response> exchange(HttpEntity<String> entity) {
        HttpMethod httpMethod = HttpMethod.resolve(method);
        return restTemplate.exchange(url, httpMethod == null ? HttpMethod.POST : httpMethod, entity, Hook.Response.class);
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