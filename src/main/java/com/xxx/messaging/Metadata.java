package com.xxx.messaging;

import lombok.Data;
import lombok.Getter;

@Data
class Metadata {
    private Replay replay;

    @Getter
    static class Replay {
        private int n;
        private Step step;
        private int skip;

        private Replay(int n, Step step, int skip) {
            this.n = n;
            this.step = step;
            this.skip = skip;
        }
    }

    void setReplay(Step step, int skip) {
        replay = new Replay(replay == null ? 1 : replay.n+1, step, skip);
    }
}