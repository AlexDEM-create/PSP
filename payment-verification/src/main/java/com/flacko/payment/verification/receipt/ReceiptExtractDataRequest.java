package com.flacko.payment.verification.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReceiptExtractDataRequest(@JsonProperty(FILE) byte[] file,
                                        @JsonProperty(PATTERNS) List<String> patterns) {

    private static final String FILE = "file";
    private static final String PATTERNS = "patterns";

}
