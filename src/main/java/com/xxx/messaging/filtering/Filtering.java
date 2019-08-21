package com.xxx.messaging.filtering;

import com.xxx.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Filtering extends Phase {
    private List<Filter> filters;

    @Autowired
    public Filtering(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public Notification execute(Context context, Notification notification) {
        for (Filter filter : filters) {
            if (!filter.accept(context, notification)) {
                log.info(String.format("Message (id: %s, group: %s) filtered by '%s' filter", notification.getTopic(), notification.getId(), filter.name()));
                context.setStatus(Status.ABORT);

                return notification;
            }
        }

        context.setStatus(Status.OK);
        return notification;
    }

    @Override
    public String name() {
        return "filtering";
    }
}
