package lmc.stage.springprojectstage.Controllers;

import lmc.stage.springprojectstage.entities.Role;
import lmc.stage.springprojectstage.services.RoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping({"/role"})
public class RoleController {


    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Role> getList() {
        return roleService.getListRole();
    }


    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public Role findbyId(@PathVariable Integer id) {

        return roleService.findById(id);
    }

    @RequestMapping(method=RequestMethod.POST)
    public void addRole(@RequestBody Role role){
        roleService.addRole(role);
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public void updateRole(@RequestBody Role role) {
        roleService.addRole(role);
    }
}
