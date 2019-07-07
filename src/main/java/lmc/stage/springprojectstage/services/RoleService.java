package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.Role;

import java.util.List;

public interface RoleService {

    Role addRole (Role role) ;
    List<Role> getListRole () ;
    void deleteRole (Integer roleId) ;
    Role findById (Integer id) ;
}
