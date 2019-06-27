package com.xxx.messaging.filtering.filter;

import com.xxx.messaging.Messaging;
import com.xxx.messaging.filtering.Filter;

class TokenUnavailable implements Filter {

    @Override
    public boolean accept(Messaging message) {
        return true;
    }

    @Override
    public String name() {
        return "Token Unavailable";
    }
}
