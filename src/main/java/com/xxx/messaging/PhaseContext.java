package com.xxx.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class PhaseContext {

    @Builder.Default
    private Map<String, ?> attributes = new HashMap<>();

    @Builder.Default
    private Status status = Status.OK;

    private Hook before;
    private Hook after;
}