package mycourse.payloads;

import mycourse.entity.ForumPost;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/20
 * @Todo:
 */
public class ForumPostResponse {
    private Long id;

    private String name;

    private String username;

    private String desc;

    private String createdAt;

    private String parentName;

    public ForumPostResponse(ForumPost forumPost, String parentName) {
        this.id = forumPost.getId();
        this.createdAt = forumPost.getCreatedAt().toString();
        this.name = forumPost.getName();
        this.desc = forumPost.getDescription();
        this.username = forumPost.getUser().getName();
        this.parentName = parentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
