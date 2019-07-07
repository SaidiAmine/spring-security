package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.TasksRepository;
import lmc.stage.springprojectstage.entities.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service(value= "TaskService")
public class TaskServiceImp implements TaskService {

    @Autowired
    TasksRepository tasksRepository ;


    @Override
    public Tasks addTasks(Tasks tasks) {
        return tasksRepository.save(tasks);
    }

    @Override
    public List<Tasks> getListTasks() {
        return tasksRepository.findAll();
    }

    @Override
    public void deleteTasks(Integer id) {
        Tasks tasks = tasksRepository.getOne(id);
        if(tasks != null)
        {
            tasksRepository.delete(tasks);
        }
    }

    @Override
    public Tasks findById(Integer id) {
        Tasks tasks = tasksRepository.getOne(id);
            return tasks;
    }

    @Override
    public  List<TaskService> getTasksByUserId (Integer id)
    {
        return null ;
    }

}
