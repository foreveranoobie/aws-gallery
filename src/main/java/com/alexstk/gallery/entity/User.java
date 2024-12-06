package com.alexstk.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String password;

    @OneToOne
    @JoinColumn(name = "role", referencedColumnName = "id")
    private UserRole role;
}
