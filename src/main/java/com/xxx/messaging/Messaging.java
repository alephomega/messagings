package com.xxx.messaging;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Messaging {
    private int replay = 0;

    private String group;
    private String id;

    private Callbacks partitioning;
    private Callbacks filtering;
    private Callbacks sending;

    private List<String> to;
    private Map<String, ?> data;
}