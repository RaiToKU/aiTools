package com.szjlc.smt.ai.sdk.executor.model.google.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class PalmConfig {

    @Getter
    @Setter
    private String apiHost = "https://generativelanguage.googleapis.com/";

    @Getter
    @Setter
    private String apiKey;

}
