package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CitationMetadata {
    private List<CitationSource> citationSources;
}
