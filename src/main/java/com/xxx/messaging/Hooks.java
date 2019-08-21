package com.xxx.messaging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Hooks {
    private Hook before;
    private Hook after;
}
