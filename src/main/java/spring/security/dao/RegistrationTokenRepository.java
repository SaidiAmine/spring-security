package spring.security.dao;

import spring.security.entities.RegistrationConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationConfirmationToken, Long> {
    RegistrationConfirmationToken findByConfirmationToken(String token);
}
