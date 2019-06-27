package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;

public class OK extends Hook {
    private static final OK singleton = new OK();
    private OK() {
        super(Status.OK);
    }

    @Override
    public Status call(String message) {
        return Status.OK;
    }

    public static OK getInstance() {
        return singleton;
    }
}