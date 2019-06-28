package lmc.stage.springprojectstage.entities;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id ;
    private String email ;
    private String name ;
    private String username ;
    private int mobile1 ;
    private int mobile2 ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role ;
    private String password ;
    private String urlWeb ;

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

    public User(String email, String name, String username, int mobile1, int mobile2, Role role, String password, String urlWeb) {
        this.email = email;
        this.name = name;
        this.username = username;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.role = role;
        this.password = password;
        this.urlWeb = urlWeb;
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
}
