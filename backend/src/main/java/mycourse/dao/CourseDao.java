package mycourse.dao;

import mycourse.entity.Course;
import mycourse.entity.ReleasedCourse;
import mycourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
@Repository
public interface CourseDao extends CrudRepository<Course, String>, JpaSpecificationExecutor<Course> {
    Optional<Course> findById(Long id);
    List<Course> findByUser(User user);
}
