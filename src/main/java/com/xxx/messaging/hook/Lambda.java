package com.xxx.messaging.hook;

import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import com.xxx.messaging.Symbol;
import lombok.Builder;
import lombok.Getter;

@Getter
@Symbol("LAMBDA")
public class Lambda extends Hook {
    private String arn;

    @Builder
    Lambda(String arn, Status onError) {
        super(onError);
        this.arn = arn;
    }

    @Override
    public Hook.Response call(String message) {
        return new Hook.Response(Status.OK);
    }
}
