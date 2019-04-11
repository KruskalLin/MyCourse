package mycourse.payloads;

import mycourse.entity.Course;
import mycourse.entity.ReleasedCourse;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
public class CourseInfo {
    private Long id;

    private String user;

    private String name;

    private String status;

    public CourseInfo(Course course) {
        this.id = course.getId();
        this.user = course.getUser().getName();
        this.name = course.getName();
        this.status = course.getStatus().name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
