package com.xxx.messaging.hook;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SNS extends AbstractHook {
    private String topicArn;

    @Builder
    SNS(String topicArn, ReturnCode onSuccess, ReturnCode onFailure) {
        super(onSuccess, onFailure);
        this.topicArn = topicArn;
    }

    @Override
    public boolean call(String id, String message) {
        return true;
    }
}