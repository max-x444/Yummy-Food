package com.nix.ua.model.enums;

public enum Status {
    ACCEPTED("accepted"),
    PENDING("pending"),
    READY("ready");

    private final String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}