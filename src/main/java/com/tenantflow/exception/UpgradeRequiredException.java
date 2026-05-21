package com.tenantflow.exception;

public class UpgradeRequiredException extends RuntimeException {

    public UpgradeRequiredException(String message) {
        super(message);
    }
}
