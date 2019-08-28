package com.xxx.messaging.hook;

import com.xxx.messaging.HookFactory;
import org.springframework.stereotype.Component;

@Component
public class SNSFactory extends HookFactory<SNS> {

    @Override
    public Class<SNS> hookClass() {
        return SNS.class;
    }

    @Override
    public SNS initialize(SNS hook) {
        return hook;
    }
}
