package com.xxx.messaging.filtering;

import com.xxx.messaging.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Filtering extends Phase {
    private List<Filter> filters;

    @Builder
    public Filtering(PhaseContext context, List<Filter> filters, Forwarder forwarder) {
        super(context, forwarder);
        this.filters = filters;
    }

    @Override
    public void execute(PhaseContext context, Messaging messaging) {
        for (Filter filter : filters) {
            if (!filter.accept(context, messaging)) {
                context.setStatus(Status.ABORT);
                log.info(String.format("Message (id: %s, group: %s) filtered by '%s' filter", messaging.getGroup(), messaging.getId(), filter.name()));

                return;
            }
        }

        context.setStatus(Status.OK);
    }
}