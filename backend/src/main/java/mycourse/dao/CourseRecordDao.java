package mycourse.dao;

import mycourse.entity.Course;
import mycourse.entity.CourseChoose;
import mycourse.entity.CourseRecord;
import mycourse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/21
 * @Todo:
 */
@Repository
public interface CourseRecordDao extends JpaRepository<CourseRecord, Long> {
    List<CourseRecord> findByCourse_User(User user);
}
