package com.xxx.messaging;

import lombok.Data;

import java.util.Map;

@Data
public class Messaging {
    private String group;
    private String id;

    private Callbacks partitioning;
    private Callbacks filtering;
    private Callbacks sending;

    private String to;
    private Map<String, ?> data;

    private int replay = 0;
}