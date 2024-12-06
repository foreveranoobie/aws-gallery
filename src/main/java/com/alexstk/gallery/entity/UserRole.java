package com.alexstk.gallery.entity;

import com.alexstk.gallery.entity.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRole {
    @Id
    private int id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private UserRoleType roleType;
}
