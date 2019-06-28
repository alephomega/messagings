package com.xxx.messaging;

import com.xxx.messaging.filtering.Filtering;
import com.xxx.messaging.hook.OK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PhaseController {
    private final Filtering filtering;

    @Autowired
    public PhaseController(Filtering filtering) {
        this.filtering = filtering;
    }

    @RequestMapping(value="/filtering", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> onMessage(@RequestHeader HttpHeaders headers, @RequestBody String body) throws Exception {
        log.info("message: {}", body);

        Messaging messaging;
        try {
            messaging = MessagingJsonSerde.fromJson(body);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        filtering.run(buildContext(messaging.getReplay(), messaging.getFiltering()), messaging);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private PhaseContext buildContext(int reply, Callbacks callbacks) {
        Hook before = OK.getInstance();
        Hook after = OK.getInstance();

        if (callbacks != null) {
            if (callbacks.getBefore() != null) {
                before = callbacks.getBefore();
            }

            if (callbacks.getAfter() != null) {
                after = callbacks.getAfter();
            }
        }

        Status status = reply > 0 ? Status.AGAIN : Status.OK;
        return PhaseContext.builder().before(before).after(after).status(status).build();
    }
}