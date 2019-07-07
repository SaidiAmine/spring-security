package lmc.stage.springprojectstage.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tasks {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id ;
    @Column
    private Date dateOrigin ;
    @Column
    private Date dateAffect ;
    @Column
    private Date dateLivraison ;
    @Column
    private Date implementationHours ;
    @Column
    private String description ;
    @Column
    private String state ;
    @Column
    private Integer taskAsked ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project ;

    @Column
    private String type ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "taskaffect")
    private User user  ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Integer getTaskAsked() {
        return taskAsked;
    }

    public void setTaskAsked(Integer taskAsked) {
        this.taskAsked = taskAsked;
    }

    public Date getDateOrigin() {
        return dateOrigin;
    }

    public void setDateOrigin(Date dateOrigin) {
        this.dateOrigin = dateOrigin;
    }

    public Date getDateAffect() {
        return dateAffect;
    }

    public void setDateAffect(Date dateAffect) {
        this.dateAffect = dateAffect;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Date getImplementationHours() {
        return implementationHours;
    }

    public void setImplementationHours(Date implementationHours) {
        this.implementationHours = implementationHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tasks() {
    }


}
