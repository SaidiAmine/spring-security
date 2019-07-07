package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.RoleRepository;
import lmc.stage.springprojectstage.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value= "RoleService")
public class RoleServiceImp implements  RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getListRole() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(Integer roleId) {
        Role role = roleRepository.getOne(roleId);
        if (role != null)
        {
            roleRepository.delete(role);
        }
    }

    @Override
    public Role findById(Integer id) {
        Role role = roleRepository.getOne(id) ;
        if (role != null)
        {
            return role ;
        }
        else {
            return null ;
        }
    }
}
