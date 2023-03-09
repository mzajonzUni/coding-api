package pl.zajonz.coding.common.exception;

import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorMessage extends ErrorMessage {

    private final List<FieldConstraintViolation> violations = new ArrayList<>();

    public ValidationErrorMessage() {
        super("Validation errors");
    }

    public void addViolation(String field, String message) {
        violations.add(new FieldConstraintViolation(field, message));
    }

    @Value
    public static class FieldConstraintViolation {
        String field;
        String message;
    }
}
