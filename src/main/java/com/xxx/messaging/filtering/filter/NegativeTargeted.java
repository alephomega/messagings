package com.xxx.messaging.filtering.filter;

import com.xxx.messaging.Messaging;
import com.xxx.messaging.filtering.Filter;

public class NegativeTargeted implements Filter {

    @Override
    public boolean accept(Messaging message) {
        return true;
    }

    @Override
    public String name() {
        return "Negative Targeted";
    }
}
