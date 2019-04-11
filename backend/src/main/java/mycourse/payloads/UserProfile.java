package mycourse.payloads;

import mycourse.entity.Role;

import java.util.Set;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/7/10
 * @Todo:
 */
public class UserProfile {
    private Long id;
    private String username;
    private Set<Role> roles;
    private String number;
    private String name;
    private String degree;
    private String grade;

    public UserProfile(Long id, String username, Set<Role> roles, String number, String name, String degree, String grade) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.number = number;
        this.name = name;
        this.degree = degree;
        this.grade = grade;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
