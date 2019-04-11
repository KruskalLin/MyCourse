package mycourse.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/8
 * @Todo:
 */
@Entity
@Table(name = "course_choose")
public class CourseChoose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "my_released_course_released_course",
            joinColumns = @JoinColumn(name = "my_released_course_id"),
            inverseJoinColumns = @JoinColumn(name = "released_course_id"))
    private ReleasedCourse releasedCourse;

    @ElementCollection
    private Map<Homework, Integer> homeworkGrades;

    private Integer grades;

    public CourseChoose() {
    }

    public Integer getGrades() {
        return grades;
    }

    public void setGrades(Integer grades) {
        this.grades = grades;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReleasedCourse getReleasedCourse() {
        return releasedCourse;
    }

    public void setReleasedCourse(ReleasedCourse releasedCourse) {
        this.releasedCourse = releasedCourse;
    }

    public Map<Homework, Integer> getHomeworkGrades() {
        return homeworkGrades;
    }

    public void setHomeworkGrades(Map<Homework, Integer> homeworkGrades) {
        this.homeworkGrades = homeworkGrades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseChoose that = (CourseChoose) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
