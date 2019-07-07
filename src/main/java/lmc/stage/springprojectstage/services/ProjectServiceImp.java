package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.dao.ProjectRepository;
import lmc.stage.springprojectstage.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
@Service(value= "ProjectService")
public class ProjectServiceImp implements ProjectService {

    @Autowired
    ProjectRepository projectRepository ;

    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getListProject() {
        return projectRepository.findAll();
    }

    @Override
    public void deleteProject(Integer id) {
        Project project = projectRepository.getOne(id) ;
        if (project != null )
        {
            projectRepository.delete(project);
        }
    }

    @Override
    public Project findById(Integer id) {
        Project project = projectRepository.getOne(id) ;
        if (project !=null)
        {
            return project ;
        }
        return null ;
    }
}
