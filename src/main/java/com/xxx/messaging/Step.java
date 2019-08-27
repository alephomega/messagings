package com.xxx.messaging;

public enum Step {
    Before,
    Process,
    After;

    public boolean before(Step step) {
        return ordinal() - step.ordinal() < 0;
    }
}
