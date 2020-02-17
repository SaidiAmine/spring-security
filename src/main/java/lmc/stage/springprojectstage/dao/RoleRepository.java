package lmc.stage.springprojectstage.dao;

import lmc.stage.springprojectstage.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Override
    Role getOne(Integer integer);
}
