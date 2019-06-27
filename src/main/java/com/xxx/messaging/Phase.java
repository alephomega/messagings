package com.xxx.messaging;

import lombok.Data;

@Data
public abstract class Phase {
    private PhaseContext context;
    private Forwarder forwarder;

    protected Phase(PhaseContext context, Forwarder forwarder) {
        this.context = context;
        this.forwarder = forwarder;
    }

    public void run(PhaseContext context, Messaging messaging) {
        before(context, messaging);
        execute(context, messaging);
        after(context, messaging);

        forward(context, messaging);
    }

    protected abstract void execute(PhaseContext context, Messaging messaging);

    private void before(PhaseContext context, Messaging messaging) {
        context.setStatus(context.getBefore().execute(messaging, context.getStatus()));
    }

    private void after(PhaseContext context, Messaging messaging) {
        context.setStatus(context.getAfter().execute(messaging, context.getStatus()));
    }

    private void forward(PhaseContext context, Messaging message) {
        forwarder.forward(context, message);
    }
}
