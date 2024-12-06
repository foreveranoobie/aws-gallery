package com.alexstk.gallery.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserGroupDto {
    ADMIN(0),
    USER(1);

    @Id
    private final int id;

    @Column(unique = true, nullable = false)
    private final String name = name();
}
