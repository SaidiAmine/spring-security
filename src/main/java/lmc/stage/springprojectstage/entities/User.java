package lmc.stage.springprojectstage.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id ;

    @Enumerated(EnumType.STRING)
    @Column
    private UserState state ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role ;


    @Column
    private String email ;
    @Column
    private String name ;
    @Column
    private String username ;
    @Column
    private int mobile1 ;
    @Column
    private int mobile2 ;
    @Column
    private String password ;
    @Column
    private String urlWeb ;
    @Column
    private Date dateCreation ;
    @Column
    private Date stateChanged ;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMobile1() {
        return mobile1;
    }

    public void setMobile1(int mobile1) {
        this.mobile1 = mobile1;
    }

    public int getMobile2() {
        return mobile2;
    }

    public void setMobile2(int mobile2) {
        this.mobile2 = mobile2;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlWeb() {
        return urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getStateChanged() {
        return stateChanged;
    }

    public void setStateChanged(Date stateChanged) {
        this.stateChanged = stateChanged;
    }

    public User(UserState state, Role role, String email, String name, String username, int mobile1, int mobile2, String password, String urlWeb, Date dateCreation, Date stateChanged) {
        this.state = state;
        this.role = role;
        this.email = email;
        this.name = name;
        this.username = username;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.password = password;
        this.urlWeb = urlWeb;
        this.dateCreation = dateCreation;
        this.stateChanged = stateChanged;
    }
}
