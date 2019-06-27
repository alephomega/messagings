package com.xxx.messaging.filtering;

import com.xxx.messaging.Messaging;

public interface Filter {
    boolean accept(Messaging message);
    String name();
}

