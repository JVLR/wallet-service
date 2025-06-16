package com.wallet.model;

public enum OperationType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    TRANSFER("TRANSFER");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
