package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Lambda extends Hook {
    private String arn;

    @Builder
    Lambda(String arn, Status onError) {
        super(onError);
        this.arn = arn;
    }

    @Override
    public Status call(String message) {
        return Status.OK;
    }
}
