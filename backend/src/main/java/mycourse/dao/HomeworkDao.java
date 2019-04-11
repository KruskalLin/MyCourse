package mycourse.dao;

import mycourse.entity.ForumTopic;
import mycourse.entity.Homework;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/22
 * @Todo:
 */
@Repository
public interface HomeworkDao extends CrudRepository<Homework, String>, JpaSpecificationExecutor<Homework> {
    Optional<Homework> findById(Long id);
}