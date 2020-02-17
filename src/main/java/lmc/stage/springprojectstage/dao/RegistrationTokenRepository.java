package lmc.stage.springprojectstage.dao;

import lmc.stage.springprojectstage.entities.RegistrationConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationConfirmationToken, String> {
    RegistrationConfirmationToken findByConfirmationToken(String token);
}
