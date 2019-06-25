package com.xxx.messaging.filtering;

import com.xxx.messaging.Message;

public interface Filter {
    boolean accept(Message message);
    String name();
}

