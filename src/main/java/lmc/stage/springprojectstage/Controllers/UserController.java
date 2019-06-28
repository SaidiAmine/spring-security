package lmc.stage.springprojectstage.Controllers;

import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService ;

    @RequestMapping(method= RequestMethod.GET)
    public List<User> getList() {
        return userService.getListUser();
    }

}
