package mycourse.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/5
 * @Todo:
 */
@Entity
@Table(name = "released_course")
public class ReleasedCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "released_course_course",
            joinColumns = @JoinColumn(name = "released_course_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Course course;

    @OneToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "released_course_homework",
            joinColumns = @JoinColumn(name = "released_course_id"),
            inverseJoinColumns = @JoinColumn(name = "homework_id"))
    private Set<Homework> homework;

    @ManyToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "released_course_user",
            joinColumns = @JoinColumn(name = "released_course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> user;

    private int limitPeople;

    private boolean showGrades;

    private Instant startTime;

    private Instant endTime;

    @Enumerated
    private CourseStatus courseStatus;

    @ElementCollection
    @CollectionTable(
            name = "course_time",
            joinColumns = @JoinColumn(name = "course_choose_id"))
    @Column(name = "course_time")
    private Set<String> courseTime;

    public ReleasedCourse() {
    }

    public boolean isShowGrades() {
        return showGrades;
    }

    public void setShowGrades(boolean showGrades) {
        this.showGrades = showGrades;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Homework> getHomework() {
        return homework;
    }

    public void setHomework(Set<Homework> homework) {
        this.homework = homework;
    }

    public Integer getLimitPeople() {
        return limitPeople;
    }

    public void setLimitPeople(int limitPeople) {
        this.limitPeople = limitPeople;
    }

    public Set<String> getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(Set<String> courseTime) {
        this.courseTime = courseTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReleasedCourse that = (ReleasedCourse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
