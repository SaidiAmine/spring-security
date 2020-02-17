package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.RegistrationTokenRepository;
import lmc.stage.springprojectstage.dao.RoleRepository;
import lmc.stage.springprojectstage.dao.UserRepository;
import lmc.stage.springprojectstage.entities.RegistrationConfirmationToken;
import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.security.service.EmailSenderService;
import lmc.stage.springprojectstage.utils.EmailExistsException;
import lmc.stage.springprojectstage.utils.UsernameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service(value= "UserService")
public class UserServiceImp implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoderBean;

    @Autowired
    private EmailSenderService emailSenderService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    @Override
    public User registerNewUserAccount(User user) throws EmailExistsException, UsernameExistsException {
        if(emailExist(user.getEmail())) throw new EmailExistsException("You have entered and email that already exists: "+user.getEmail());
        if(usernameExist(user.getUsername())) throw new UsernameExistsException("You have entered a username that already exists: "+user.getUsername());
        logger.info("Inside service, registration method");
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setRole(roleRepository.getOne(1));
        newUser.setEmail(user.getEmail());
        newUser.setDateCreation(new Date());
        newUser.setPassword(passwordEncoderBean.encode(user.getPassword()));
        newUser.setEnabled(false);
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        userRepository.save(newUser);

        RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(newUser);
        registrationTokenRepository.save(registrationConfirmationToken);

        sendConfirmationToken(user, registrationConfirmationToken);

        return newUser;
    }

    private void sendConfirmationToken(User user, RegistrationConfirmationToken token){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("saidii.aamine@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/user/confirm-account?token="+token.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
    }

    private boolean emailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    private boolean usernameExist(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null)
            return true;
        return false;
    }

    /*@Override
    public User registerNewUserAccount(User user) throws EmailExistsException {
        return null;
    }*/

    /**
     *
     *
     *
     *
     * **/
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
