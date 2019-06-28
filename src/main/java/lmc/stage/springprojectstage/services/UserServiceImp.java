package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.UserRepository;
import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.entities.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service(value= "userService")
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

    public UserServiceImp() {
    }

    @Override
    public User addUser(User user) {

        user.setState(UserState.valide);
        user.setDateCreation(new Date());
        return userRepository.save(user);
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Integer id) {
        User user = findById(id);
        if (user != null)
        {
            userRepository.delete(user);
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

}
