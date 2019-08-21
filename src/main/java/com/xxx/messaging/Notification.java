package com.xxx.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class Notification {
    private String topic;
    private String id;
    private String collapseKey;

    private String to;
    private Map<String, ?> message;
}
