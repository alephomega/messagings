package com.xxx.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.messaging.filtering.Filtering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class FilteringController {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping()
            .create();

    private final Filtering filtering;

    @Autowired
    public FilteringController(Filtering filtering) {
        this.filtering = filtering;
    }

    @RequestMapping(value="/message", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> onMessage(@RequestHeader HttpHeaders headers, @RequestBody String body) throws Exception {
        log.info("message: {}", body);

        Message message;
        try {
            message = GSON.fromJson(body, Message.class);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        filtering.run(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
