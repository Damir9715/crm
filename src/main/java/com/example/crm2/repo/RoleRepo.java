package com.example.crm2.repo;

import com.example.crm2.model.user.Role;
import com.example.crm2.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleName roleName);
}
