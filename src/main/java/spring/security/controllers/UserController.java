package spring.security.controllers;


import spring.security.entities.http.models.UserRegistration;
import spring.security.services.UserService;
import spring.security.exceptions.EmailExistsException;
import spring.security.exceptions.UsernameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping({"/user"})
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistration user) throws EmailExistsException, UsernameExistsException {
        logger.info("Inside: /registration");
        userService.registerNewUserAccount(user);
        return ok().body("{\"User successfuly added.\"}");
    }

    @PostMapping(value = "/confirm-account")
    public ResponseEntity<Object> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        userService.activateUserAccount(confirmationToken);
        return ResponseEntity.ok("{Account activated.}");
    }

}