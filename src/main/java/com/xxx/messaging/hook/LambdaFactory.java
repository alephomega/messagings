package com.xxx.messaging.hook;

import com.xxx.messaging.HookFactory;
import org.springframework.stereotype.Component;

@Component
public class LambdaFactory extends HookFactory<Lambda> {

    @Override
    public Class<Lambda> hookClass() {
        return Lambda.class;
    }

    @Override
    public Lambda initialize(Lambda hook) {
        return hook;
    }
}
