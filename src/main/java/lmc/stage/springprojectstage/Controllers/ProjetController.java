package lmc.stage.springprojectstage.Controllers;


import lmc.stage.springprojectstage.entities.Project;
import lmc.stage.springprojectstage.services.ProjectService;
import lmc.stage.springprojectstage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/project"})
public class ProjetController {

    @Autowired
    public ProjectService projectService;

    @Autowired
    public UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Project> getListProject ()
    {
        return projectService.getListProject();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Project getById (@PathVariable Integer id)
    {
        return projectService.findById(id) ;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addProject (@RequestBody Project project)
    {
        projectService.addProject(project);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public void updateProject (@RequestBody Project project)
    {
        projectService.addProject(project);
    }

}
