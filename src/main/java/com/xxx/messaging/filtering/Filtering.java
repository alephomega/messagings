package com.xxx.messaging.filtering;

import com.xxx.messaging.*;
import lombok.Builder;

import java.util.List;

public class Filtering extends Phase {
    private List<Filter> filters;

    @Builder
    public Filtering(PhaseContext context, List<Filter> filters, Forwarder forwarder) {
        super(context, forwarder);
        this.filters = filters;
    }

    @Override
    public void execute(PhaseContext context, Messaging message) {
        for (Filter filter : filters) {
            if (!filter.accept(message)) {
                context.setStatus(Status.ABORT);
                return;
            }
        }

        context.setStatus(Status.OK);
    }
}