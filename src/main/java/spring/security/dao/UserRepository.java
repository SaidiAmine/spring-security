package spring.security.dao;
import spring.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
   User findByUsername(String username);
}
