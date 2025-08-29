package com.manager.finance.entity;


public enum PermissionEntity {
    ALL_READ("all:read"),
    ALL_DELETE("all:delete"),
    ALL_WRITE("all:write"),
    USER_READ("user:read"),
    USER_DELETE("user:delete"),
    USER_WRITE("user:write"),
    ROLE_CRUD("role:crud");

    private final String permission;

    PermissionEntity(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
