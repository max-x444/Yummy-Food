package com.nix.ua.model.enums;

public enum Role {
    ADMIN("admin"),
    GUEST("guest"),
    USER("user");

    private final String title;

    Role(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}