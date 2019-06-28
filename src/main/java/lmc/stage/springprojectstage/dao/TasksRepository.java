package lmc.stage.springprojectstage.dao;

import lmc.stage.springprojectstage.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks,Integer> {
}
