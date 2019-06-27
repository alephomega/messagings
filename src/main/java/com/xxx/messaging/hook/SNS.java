package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SNS extends Hook {
    private String topicArn;

    @Builder
    SNS(String topicArn, Status onError) {
        super(onError);
        this.topicArn = topicArn;
    }

    @Override
    public Status call(String message) {
        return Status.OK;
    }
}