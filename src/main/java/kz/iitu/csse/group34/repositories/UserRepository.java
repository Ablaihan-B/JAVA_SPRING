package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Roles;
import kz.iitu.csse.group34.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    Users findByDeletedAtNullAndEmail(String email);
    boolean existsByEmail(String email);
    List<Users> findAllByRolesNotLike(Roles roles);
}
