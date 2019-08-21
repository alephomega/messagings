package com.xxx.messaging.partitioning;

import com.xxx.messaging.Context;
import com.xxx.messaging.Notification;
import com.xxx.messaging.Phase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Partitioning extends Phase {
    private final Partitioner partitioner;

    @Autowired
    public Partitioning(Partitioner partitioner) {
        this.partitioner = partitioner;
    }

    @Override
    public Notification execute(Context context, Notification notification) {
        return partitioner.run(notification);
    }

    @Override
    public String name() {
        return "partitioning";
    }
}