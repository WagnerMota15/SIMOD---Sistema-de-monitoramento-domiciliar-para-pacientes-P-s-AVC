package com.example.simodapp.domain.result;

public class VerificationResult {

    private final boolean approved;
    private final String message;

    private VerificationResult(boolean approved, String message) {
        this.approved = approved;
        this.message = message;
    }

    public static VerificationResult approved() {
        return new VerificationResult(true, null);
    }

    public static VerificationResult rejected(String message) {
        return new VerificationResult(false, message);
    }

    public boolean isApproved() {
        return approved;
    }

    public String getMessage() {
        return message;
    }
}