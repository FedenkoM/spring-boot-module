package org.example.model;


import lombok.Getter;

import java.util.Set;

public enum Role {
    ROLE_USER(Set.of(Permission.READ)),
    ROLE_ADMIN(Set.of(Permission.READ, Permission.WRITE, Permission.UPDATE));

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Getter
    private final Set<Permission> permissions;
}
