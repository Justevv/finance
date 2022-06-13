package com.manager.finance.entity;

public enum Permission {
    ALL("ALL");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
