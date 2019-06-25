package com.xxx.messaging.filtering.filter;

import com.xxx.messaging.Message;
import com.xxx.messaging.filtering.Filter;

class TokenUnavailable implements Filter {

    @Override
    public boolean accept(Message message) {
        return true;
    }

    @Override
    public String name() {
        return "Token Unavailable";
    }
}
