package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.User;

import java.util.List;

public interface UserService {

    User addUser (User user);

    List<User> getListUser();

    void deleteUser(Integer id);

    User findById(Integer id);


}
