package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.Tasks;

import java.util.List;

public interface TaskService {

    Tasks addTasks (Tasks tasks) ;
    List<Tasks> getListTasks () ;
    void deleteTasks (Integer id) ;
    Tasks findById (Integer id);
    List<TaskService> getTasksByUserId (Integer id);

}
