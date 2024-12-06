package com.alexstk.gallery.dto;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class UserDto {
    private final int id;
    private final String name;
    private final String password;
    private final UserGroupDto group;

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String password() {
        return password;
    }

    public UserGroupDto group() {
        return group;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDto) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, group);
    }

    @Override
    public String toString() {
        return "UserDto[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "password=" + password + ", " +
                "group=" + group + ']';
    }

}
