package mycourse.dao;

import mycourse.entity.CourseChoose;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/21
 * @Todo:
 */
@Repository
public interface CourseChooseDao extends CrudRepository<CourseChoose, String>, JpaSpecificationExecutor<CourseChoose> {

}