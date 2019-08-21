package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import com.xxx.messaging.Symbol;

@Symbol("OK")
public class OK extends Hook {
    private static final OK singleton = new OK();
    private OK() {
        super(Status.OK);
    }

    @Override
    public Hook.Response call(String message) {
        return new Hook.Response(Status.OK);
    }

    public static OK getInstance() {
        return singleton;
    }
}