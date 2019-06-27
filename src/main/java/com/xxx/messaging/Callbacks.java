package com.xxx.messaging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Callbacks {
    private Hook before;
    private Hook after;
}
