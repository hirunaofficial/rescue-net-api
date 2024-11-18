package dev.hiruna.rescuenet.repository;

import dev.hiruna.rescuenet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}