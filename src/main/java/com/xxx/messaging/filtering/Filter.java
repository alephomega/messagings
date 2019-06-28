package com.xxx.messaging.filtering;

import com.xxx.messaging.Messaging;
import com.xxx.messaging.PhaseContext;

public interface Filter {
    boolean accept(PhaseContext context, Messaging messaging);
    String name();
}

