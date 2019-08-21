package com.xxx.messaging.filtering;

import com.xxx.messaging.Context;
import com.xxx.messaging.Notification;

public interface Filter {
    boolean accept(Context context, Notification notification);
    String name();
}