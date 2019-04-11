package mycourse.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/5
 * @Todo:
 */
@Entity
@Table(name = "post")
public class ForumPost extends ForumTopic{

    public ForumPost() {
    }

    public ForumPost(ForumTopic forumTopic) {
        this.setId(forumTopic.id);
        this.setCreatedAt(forumTopic.createdAt);
        this.setDescription(forumTopic.description);
        this.setName(forumTopic.name);
        this.setUser(forumTopic.user);
        this.setPosts(forumTopic.posts);
    }


}
