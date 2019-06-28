package lmc.stage.springprojectstage.dao;

import lmc.stage.springprojectstage.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {

}
