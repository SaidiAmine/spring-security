package lmc.stage.springprojectstage.Controllers;

import lmc.stage.springprojectstage.entities.Tasks;
import lmc.stage.springprojectstage.services.ProjectService;
import lmc.stage.springprojectstage.services.TaskService;
import lmc.stage.springprojectstage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/task"})
public class TaskController {

    @Autowired
    TaskService taskService ;
    @Autowired
    UserService userService ;
    @Autowired
    ProjectService projectService ;

    @RequestMapping(method = RequestMethod.GET)
    public List<Tasks> getListTasks ()
    {
        return taskService.getListTasks();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Tasks findById (@PathVariable Integer id)
    {
        return taskService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addTask (@RequestBody Tasks tasks)
    {
        // el user eli 3mal demande
        tasks.setTaskAsked(1);
        tasks.setUser(userService.findById(tasks.getUser().getId()));
        tasks.setProject(projectService.findById(tasks.getProject().getId()));
        taskService.addTasks(tasks);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public void updateTask (@RequestBody Tasks tasks)
    {
        tasks.setTaskAsked(1);
        tasks.setUser(userService.findById(tasks.getUser().getId()));
        tasks.setProject(projectService.findById(tasks.getProject().getId()));
        taskService.addTasks(tasks);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void deleteTask (@PathVariable Integer id)
    {
        taskService.deleteTasks(id);
    }
}
