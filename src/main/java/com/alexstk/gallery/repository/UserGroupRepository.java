package com.alexstk.gallery.repository;

import com.alexstk.gallery.entity.UserRole;
import com.alexstk.gallery.entity.enums.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserRole, Integer> {
}
