package com.xxx.messaging;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Message {
    private String group;
    private String id;

    private Phase.Callbacks partitioning;
    private Phase.Callbacks filtering;
    private Phase.Callbacks sending;

    private List<String> to;
    private Map<String, ?> data;
}