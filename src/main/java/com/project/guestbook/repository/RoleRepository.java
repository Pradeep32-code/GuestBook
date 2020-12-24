package com.project.guestbook.repository;

import com.project.guestbook.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is Repository class is used to save different role of user in database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

}
