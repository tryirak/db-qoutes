package com.dbquotes.models;

public enum UserRole {
    SUPERUSER,
    MODERATOR,
    USER,
    GUEST;

    public static UserRole getRole(String roleName) {
        return switch (roleName) {
            case "SUPERUSER" -> SUPERUSER;
            case "MODERATOR" -> MODERATOR;
            case "USER" -> USER;
            case "GUEST" -> GUEST;
            default -> throw new IllegalStateException("Unexpected value: " + roleName);
        };
    }
}
