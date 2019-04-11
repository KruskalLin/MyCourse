package mycourse.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/8
 * @Todo:
 */
@Entity
@Table(name = "course_record")
public class CourseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Instant createdAt;

    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "course_course_record",
            joinColumns = @JoinColumn(name = "course_record_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Course course;

    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_course_record",
            joinColumns = @JoinColumn(name = "course_record_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @Enumerated
    private RecordOperation operation;


    public CourseRecord() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public RecordOperation getOperation() {
        return operation;
    }

    public void setOperation(RecordOperation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRecord that = (CourseRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
