package lmc.stage.springprojectstage.Controllers;


import lmc.stage.springprojectstage.entities.User;
import lmc.stage.springprojectstage.entities.UserState;
import lmc.stage.springprojectstage.services.ProjectService;
import lmc.stage.springprojectstage.services.RoleService;
import lmc.stage.springprojectstage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping({"/userr"})
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService ;
    @Autowired
    private ProjectService projectService ;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getList() {
        return userService.getListUser() ;
    }

    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public User findbyId(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @RequestMapping(method=RequestMethod.POST)
    public void addUser(@RequestBody User user) {
        user.setRole(roleService.findById(user.getRole().getId()));
      //  user.setProject(projectService.findById(user.getProject().getId()));
        userService.addUser(user);
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public void updateUser(@RequestBody User user) {
        user.setRole(roleService.findById(user.getRole().getId()));
        userService.addUser(user);
    }

    @RequestMapping(value="/blocked",method=RequestMethod.PUT)
    public void BlockUser(@RequestBody User user) {
       if(user.getState() == UserState.valide)
       {
           user.setState(UserState.blocked);
           userService.addUser(user);
       }
       else
           if(user.getState() == UserState.blocked){
           user.setState(UserState.valide);
           userService.addUser(user);
       }
    }

}


