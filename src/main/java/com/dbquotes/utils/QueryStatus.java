package com.dbquotes.utils;

public enum QueryStatus {
    DONE,
    NO_CONNECTION,
    DUPLICATE,
    NO_ENTRY,
    UNKNOWN,
    CUSTOM,
    NO_PERMISSIONS;

    public String getText() {
        return switch(this) {
            case NO_CONNECTION -> "No connection.";
            case UNKNOWN -> "Unknown error! .";
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }
}
