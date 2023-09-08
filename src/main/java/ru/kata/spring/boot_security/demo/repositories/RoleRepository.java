package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findByIdIn(Set<Long> ids);
    Set<Role> getRolesByIdIn(Set<Long> id);
}
