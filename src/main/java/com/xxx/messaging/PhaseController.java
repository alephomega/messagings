package com.xxx.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
abstract class PhaseController {
    private final Processor processor;

    PhaseController(Processor processor) {
        this.processor = processor;
    }

    ResponseEntity<String> process(String body) {
        log.info("message: {}", body);

        Messaging messaging;
        try {
            messaging = JsonSerde.jsonToMessaging(body);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        processor.process(messaging);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
