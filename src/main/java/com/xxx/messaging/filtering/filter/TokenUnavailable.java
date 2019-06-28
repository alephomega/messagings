package com.xxx.messaging.filtering.filter;

import com.xxx.messaging.Messaging;
import com.xxx.messaging.PhaseContext;
import com.xxx.messaging.filtering.Filter;

class TokenUnavailable implements Filter {

    @Override
    public boolean accept(PhaseContext context, Messaging messaging) {
        return true;
    }

    @Override
    public String name() {
        return "Token Unavailable";
    }
}
