package spring.security.services;

import spring.security.entities.User;
import spring.security.entities.http.models.UserRegistration;
import spring.security.exceptions.EmailExistsException;
import spring.security.exceptions.UsernameExistsException;

import java.util.List;

public interface UserService {

    User addUser (User user);

    List<User> getListUser();

    void deleteUser(Long id);

    User findById(Long id);

    User registerNewUserAccount(UserRegistration user) throws EmailExistsException, UsernameExistsException;

    User activateUserAccount(String confirmationToken);

}
