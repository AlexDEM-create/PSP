package com.flacko.payment.verification.receipt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReceiptExtractDataRequest(@JsonProperty(FILE_ABSOLUTE_PATH) String fileAbsolutePath,
                                        @JsonProperty(PATTERNS) List<String> patterns) {

    private static final String FILE_ABSOLUTE_PATH = "file_absolute_path";
    private static final String PATTERNS = "patterns";

}
