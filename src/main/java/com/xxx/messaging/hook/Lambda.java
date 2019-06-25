package com.xxx.messaging.hook;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Lambda extends AbstractHook {
    private String arn;

    @Builder
    Lambda(String arn, ReturnCode onSuccess, ReturnCode onFailure) {
        super(onSuccess, onFailure);
        this.arn = arn;
    }

    @Override
    public boolean call(String id, String message) {
        return true;
    }
}
