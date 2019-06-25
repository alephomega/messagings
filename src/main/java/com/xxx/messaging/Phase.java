package com.xxx.messaging;

import lombok.Data;

import java.util.Map;

public abstract class Phase {
    private final Callbacks callbacks;
    private final Forwarder forwarder;

    protected Phase(Callbacks callbacks, Forwarder forwarder) {
        this.callbacks = callbacks;
        this.forwarder = forwarder;
    }

    protected Hook.ReturnCode before(Message message, Map<String, ?> args) {
        return callbacks.execute(callbacks.getBefore(), message, args);
    }

    protected Hook.ReturnCode after(Message message, Map<String, ?> args) {
        return callbacks.execute(callbacks.getAfter(), message, args);
    }

    protected Hook.ReturnCode error(Message message, Map<String, ?> args) {
        return callbacks.execute(callbacks.getError(), message, args);
    }

    protected void forward(Message message) {
        forwarder.forward(message);
    }

    public abstract void run(Message message);


    @Data
    protected static class Callbacks {
        private Hook before;
        private Hook after;
        private Hook error;

        public Hook.ReturnCode before(Message message, Map<String, ?> args) {
            return execute(before, message, args);
        }

        public Hook.ReturnCode after(Message message, Map<String, ?> args) {
            return execute(after, message, args);
        }

        public Hook.ReturnCode error(Message message, Map<String, ?> args) {
            return execute(error, message, args);
        }

        private Hook.ReturnCode execute(Hook hook, Message message, Map<String, ?> args) {
            return hook == null ? Hook.ReturnCode.OK : hook.execute(message, args);
        }
    }
}
