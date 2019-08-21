package com.xxx.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PartitioningController extends PhaseController {

    @Autowired
    public PartitioningController(@Qualifier("partitioningProcessor") Processor processor) {
        super(processor);
    }

    @RequestMapping(value="/phases/partitioning", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> doPartitioning(@RequestHeader HttpHeaders headers, @RequestBody String body) {
        return process(body);
    }
}