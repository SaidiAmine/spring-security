package spring.security.services;

import spring.security.dao.RegistrationTokenRepository;
import spring.security.dao.RoleRepository;
import spring.security.dao.UserRepository;
import spring.security.entities.RegistrationConfirmationToken;
import spring.security.entities.User;
import spring.security.entities.http.models.UserRegistration;
import spring.security.exceptions.BusinessException;
import spring.security.security.service.EmailSenderService;
import spring.security.exceptions.EmailExistsException;
import spring.security.exceptions.UsernameExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Value("${server.port}")
    private String serverPort;

    //@Transactional
    @javax.transaction.Transactional
    @Override
    public User registerNewUserAccount(UserRegistration user) throws EmailExistsException, UsernameExistsException {
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
        newUser.setFirstname(user.getFirstName());
        newUser.setLastname(user.getLastName());
        userRepository.save(newUser);

        RegistrationConfirmationToken registrationConfirmationToken = new RegistrationConfirmationToken(newUser);
        registrationTokenRepository.save(registrationConfirmationToken);

        //sendConfirmationToken(user, registrationConfirmationToken);

        return newUser;
    }

    private void sendConfirmationToken(UserRegistration user, RegistrationConfirmationToken token){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("saidii.aamine@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:" + serverPort + "/user/confirm-account?token="+token.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
    }

    @Override
    public User activateUserAccount(String confirmationToken) {
        RegistrationConfirmationToken registrationToken = registrationTokenRepository.findByConfirmationToken(confirmationToken);
        if (registrationToken != null) {
            Optional<User> userOp = userRepository.findByEmail(registrationToken.getUser().getEmail());
            if (userOp.isPresent()) {
                User user = userOp.get();
                user.setEnabled(true);
                userRepository.save(user);
                return user;
            } else {
                throw new BusinessException(String.format("No user found for %s", registrationToken.getUser().getEmail()));
            }
        } else {
            throw new BusinessException("No registration token found");
        }
    }

    private boolean emailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    private boolean usernameExist(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getOne(id) ;
        if (user != null )
        {
            userRepository.delete(user);
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }




}
