package com.xxx.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
class Messaging {
    private String topic;
    private String id;
    private String collapseKey;

    private String to;
    private Map<String, ?> message;

    private Callbacks callbacks;
    private Metadata metadata;

    void reset() {
        this.metadata = new Metadata();
    }

    @Setter
    @Getter
    static class Callbacks {
        private Hooks partitioning;
        private Hooks filtering;
        private Hooks sending;
    }

    @Setter
    @Getter
    static class Metadata {
        private int replay = 0;
    }
}