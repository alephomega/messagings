package com.xxx.messaging.hook;

import com.xxx.messaging.HookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HTTPFactory extends HookFactory<HTTP> {
    private final RestTemplate restTemplate;

    @Autowired
    public HTTPFactory(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Class<HTTP> hookClass() {
        return HTTP.class;
    }

    @Override
    public HTTP initialize(HTTP hook) {
        hook.setRestTemplate(restTemplate);
        return hook;
    }
}
