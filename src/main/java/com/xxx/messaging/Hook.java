package com.xxx.messaging;

import java.util.Map;

public interface Hook {
    ReturnCode execute(Message message, Map<String, ?> args);

    enum ReturnCode {
        OK,
        AGAIN,
        ABORT,
        DONE
    }
}