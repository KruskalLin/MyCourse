package mycourse.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/5
 * @Todo:
 */
@Entity
@Table(name = "forum")
public class Forum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "forum_topics",
            joinColumns = @JoinColumn(name = "forum_id"),
            inverseJoinColumns = @JoinColumn(name = "topics_id"))
    private Set<ForumTopic> topics;

    public Forum() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ForumTopic> getTopics() {
        return topics;
    }

    public void setTopics(Set<ForumTopic> topics) {
        this.topics = topics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Forum forum = (Forum) o;
        return Objects.equals(id, forum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
