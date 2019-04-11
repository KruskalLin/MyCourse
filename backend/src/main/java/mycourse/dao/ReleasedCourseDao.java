package mycourse.dao;

import mycourse.entity.Course;
import mycourse.entity.ReleasedCourse;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/20
 * @Todo:
 */
@Repository
public interface ReleasedCourseDao extends CrudRepository<ReleasedCourse, String>, JpaSpecificationExecutor<ReleasedCourse> {
    Optional<ReleasedCourse> findById(Long id);
    List<ReleasedCourse> findByCourse(Course course);
}