package mycourse.payloads;

import mycourse.entity.ForumTopic;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
public class ForumTopicResponse {
    private Long id;

    private String name;

    private String username;

    public ForumTopicResponse(ForumTopic forumTopic) {
        this.id = forumTopic.getId();
        this.name = forumTopic.getName();
        this.username = forumTopic.getUser().getName();
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
}
