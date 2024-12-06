package com.alexstk.gallery.api.user;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public final class UserResponse {
    private final int id;
    private final String name;
    private final String role;

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String role() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserResponse) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role);
    }

    @Override
    public String toString() {
        return "UserResponse[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "role=" + role + ']';
    }

}
