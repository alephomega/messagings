package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.HookFailedException;
import com.xxx.messaging.Status;
import com.xxx.messaging.Symbol;

@Symbol("KINESIS")
public class Kinesis extends Hook {

    public Kinesis(Status onError) {
        super(onError);
    }

    @Override
    public Response call(String message) throws HookFailedException {
        return null;
    }
}