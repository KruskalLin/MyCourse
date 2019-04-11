package mycourse.dao;

import mycourse.entity.ForumTopic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/20
 * @Todo:
 */
@Repository
public interface ForumTopicDao extends CrudRepository<ForumTopic, String>, JpaSpecificationExecutor<ForumTopic> {
    Optional<ForumTopic> findById(Long id);
}
