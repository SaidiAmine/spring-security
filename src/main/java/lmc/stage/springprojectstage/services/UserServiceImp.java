package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.UserRepository;
import lmc.stage.springprojectstage.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value= "UserService")
public class UserServiceImp implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.getOne(id) ;
        if (user != null )
        {
            userRepository.delete(user);
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepository.getOne(id);
    }
}
