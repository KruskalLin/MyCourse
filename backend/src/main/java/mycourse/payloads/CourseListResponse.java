package mycourse.payloads;

import mycourse.entity.Course;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/3/18
 * @Todo:
 */
public class CourseListResponse {

    private Long id;

    private String name;

    private String username;

    private String status;

    public CourseListResponse(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.username = course.getUser().getName();
        this.status = course.getStatus().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
