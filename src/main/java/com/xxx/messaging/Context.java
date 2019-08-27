package com.xxx.messaging;

import com.xxx.messaging.hook.OK;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class Context {
    @Builder.Default
    private Status status = Status.OK;

    @Builder.Default
    private Step step = Step.Before;

    @Builder.Default
    private int iteration = 0;

    @Builder.Default
    private Metadata metadata = new Metadata();

    @Builder.Default
    private Hook before = OK.getInstance();

    @Builder.Default
    private Hook after = OK.getInstance();

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();


    Context next() {
        step = Step.Before;
        iteration++;
        return this;
    }

    Context ok(Step step) {
        status = Status.OK;
        return this;
    }

    Context abort(Step step) {
        status = Status.ABORT;
        return this;
    }

    Context again(Step step) {
        status = Status.AGAIN;
        metadata.setReplay(step, iteration);
        return this;
    }

    Context error() {
        status = Status.AGAIN;
        metadata.setReplay(step, iteration);
        return this;
    }
}