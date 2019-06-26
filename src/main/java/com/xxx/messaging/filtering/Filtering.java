package com.xxx.messaging.filtering;

import com.google.common.collect.ImmutableMap;
import com.xxx.messaging.Forwarder;
import com.xxx.messaging.Hook;
import com.xxx.messaging.Message;
import com.xxx.messaging.Phase;

import java.util.List;
import java.util.Map;

public class Filtering extends Phase {
    private final List<Filter> filters;

    public Filtering(List<Filter> filters, Callbacks callbacks, Forwarder forwarder) {
        super(callbacks, forwarder);
        this.filters = filters;
    }

    @Override
    public void run(Message message) {
        handle(before(message, null));

        Map<String, ?> args = null;
        for (Filter filter : filters) {
            if (!filter.accept(message)) {
                args = ImmutableMap.of("filtered", true, "filter", filter.name());
                break;
            }
        }

        if (args == null) {
            args = ImmutableMap.of("filtered", false);
        }

        handle(after(message, args));
        forward(message);
    }

    private void handle(Hook.ReturnCode returnCode) {
        switch (returnCode) {
            case ABORT:
                abort();
                break;

            case AGAIN:
                again();
                break;

            case OK:
            case DONE:
        }
    }

    private void again() {

    }

    private void abort() {

    }
}