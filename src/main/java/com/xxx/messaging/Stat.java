package com.xxx.messaging;

import lombok.Data;

@Data
class Stat {
    private boolean errorOccurred = false;

    private int ok;
    private int abort;
    private int again;

    void ok() {
        ok++;
    }

    void abort() {
        abort++;
    }

    void again() {
        again++;
    }

    void error() {
        errorOccurred = true;
    }
}
