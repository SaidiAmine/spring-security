package lmc.stage.springprojectstage.Controllers;


import lmc.stage.springprojectstage.dao.RegistrationTokenRepository;
import lmc.stage.springprojectstage.dao.UserRepository;
import lmc.stage.springprojectstage.entities.RegistrationConfirmationToken;
import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.entities.UserState;
import lmc.stage.springprojectstage.services.ProjectService;
import lmc.stage.springprojectstage.services.RoleService;
import lmc.stage.springprojectstage.services.UserService;
import lmc.stage.springprojectstage.services.UserServiceImp;
import lmc.stage.springprojectstage.utils.EmailExistsException;
import lmc.stage.springprojectstage.utils.UsernameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping({"/user"})
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody User user) throws EmailExistsException, UsernameExistsException {
        logger.info("Inside: /registration");
        userServiceImp.registerNewUserAccount(user);
        return ok().body("{\"User successfuly added.\"}");
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Object> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        RegistrationConfirmationToken token = registrationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Optional<User> userOp = userRepository.findByEmail(token.getUser().getEmail());
            User user = new User();
            if (userOp.isPresent()) {
                user = userOp.get();
            }
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            return ResponseEntity.badRequest().body("{Link is broken}");
        }

        return ResponseEntity.ok("{Account activated.]");
    }

    ////

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getList() {
        return userService.getListUser();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findbyId(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addUser(@RequestBody User user) {
        user.setRole(roleService.findById(user.getRole().getId()));
        //  user.setProject(projectService.findById(user.getProject().getId()));
        userService.addUser(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateUser(@RequestBody User user) {
        user.setRole(roleService.findById(user.getRole().getId()));
        userService.addUser(user);
    }

    @RequestMapping(value = "/blocked", method = RequestMethod.PUT)
    public void BlockUser(@RequestBody User user) {
        if (user.getState() == UserState.valide) {
            user.setState(UserState.blocked);
            userService.addUser(user);
        } else if (user.getState() == UserState.blocked) {
            user.setState(UserState.valide);
            userService.addUser(user);
        }
    }

}