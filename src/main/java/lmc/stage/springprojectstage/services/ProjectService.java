package lmc.stage.springprojectstage.services;

import lmc.stage.springprojectstage.entities.Project;

import java.util.List;

public interface ProjectService {

    Project addProject (Project project) ;
    List<Project> getListProject ();
    void deleteProject (Integer id) ;
    Project findById (Integer id) ;
}
