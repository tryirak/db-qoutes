package com.dbquotes.utility;

public enum QueryStatus {
    DONE,
    NOCONNECTION,
    DUPLICATE,
    EMPTY,
    UNKNOWN,
    CUSTOM,
    NOPERMISSIONS;

    public String getText() {
        return switch(this) {
            case NOCONNECTION -> "No connection.";
            case UNKNOWN -> "Unknown error!";
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }
}
