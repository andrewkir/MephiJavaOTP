package ru.andrewkir.model;

public enum UserRoles {
    ADMIN,
    USER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}