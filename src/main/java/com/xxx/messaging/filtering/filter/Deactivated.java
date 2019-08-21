package com.xxx.messaging.filtering.filter;

import com.xxx.messaging.Context;
import com.xxx.messaging.Notification;
import com.xxx.messaging.filtering.Filter;
import org.springframework.stereotype.Component;

@Component
public class Deactivated implements Filter {

    @Override
    public boolean accept(Context context, Notification notification) {
        return true;
    }

    @Override
    public String name() {
        return "Deactivated";
    }
}
