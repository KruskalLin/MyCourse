package mycourse.payloads;

import mycourse.entity.ReleasedCourse;

import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/21
 * @Todo:
 */
public class ChooseResponse {
    private Long id;

    private String name;

    private boolean hasChoosed;

    private int choosed;

    private int limit;

    private String time;

    private String courseState;

    public ChooseResponse(ReleasedCourse releasedCourse) {
        this.id = releasedCourse.getId();
        this.courseState = releasedCourse.getCourseStatus().name();
        this.hasChoosed = false;
        this.choosed = releasedCourse.getUser().size();
        this.limit = releasedCourse.getLimitPeople();
        this.name = releasedCourse.getCourse().getName();
        this.time = releasedCourse.getCourseTime().stream().collect(Collectors.joining("\n"));
    }

    public int getChoosed() {
        return choosed;
    }

    public void setChoosed(int choosed) {
        this.choosed = choosed;
    }

    public boolean isHasChoosed() {
        return hasChoosed;
    }

    public void setHasChoosed(boolean hasChoosed) {
        this.hasChoosed = hasChoosed;
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
