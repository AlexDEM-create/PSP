package service.exception;

import java.util.Optional;

public class StatsMissingRequiredAttributeException extends Exception {

    private final String attribute;

    public StatsMissingRequiredAttributeException(String attribute, Optional<String> id) {
        super("Stats attribute '" + attribute + "' is required" + (id.isPresent() ? " for Stats with id " + id.get() : ""));
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
