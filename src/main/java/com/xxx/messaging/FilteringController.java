package com.xxx.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class FilteringController extends PhaseController {

    @Autowired
    public FilteringController(@Qualifier("filteringProcessor") Processor processor) {
        super(processor);
    }

    @RequestMapping(value="/phases/filtering", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> doFiltering(@RequestHeader HttpHeaders headers, @RequestBody String body) {
        return process(body);
    }
}