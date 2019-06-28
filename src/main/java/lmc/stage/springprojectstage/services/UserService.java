package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser (User user);

    List<User> getListUser();

    void deleteUser(Integer id);

    User findById(Integer id);

    Optional<User> findByEmail(String nomPrenom);



}
