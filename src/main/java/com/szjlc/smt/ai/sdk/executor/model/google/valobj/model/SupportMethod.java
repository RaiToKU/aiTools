package com.szjlc.smt.ai.sdk.executor.model.google.valobj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportMethod {

    private String generateMethod;
    private String countMessageMethod;
}
