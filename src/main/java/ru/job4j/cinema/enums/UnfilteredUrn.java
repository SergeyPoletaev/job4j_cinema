package ru.job4j.cinema.enums;

public enum UnfilteredUrn {
    INDEX("index"),
    LOGIN_PAGE("loginPage"),
    LOGIN("login"),
    FORM_ADD_USER("formAddUser"),
    REGISTRATION("registration");

    private final String value;

    UnfilteredUrn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
