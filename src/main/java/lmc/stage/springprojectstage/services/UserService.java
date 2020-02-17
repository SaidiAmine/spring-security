package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.utils.EmailExistsException;
import lmc.stage.springprojectstage.utils.UsernameExistsException;

import java.util.List;

public interface UserService {

    User addUser (User user);

    List<User> getListUser();

    void deleteUser(Integer id);

    User findById(Integer id);

    User registerNewUserAccount(User user) throws EmailExistsException, UsernameExistsException;


}
