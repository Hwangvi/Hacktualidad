package com.Hacktualidad.repository;

import com.Hacktualidad.entity.ForumTopic;
import com.Hacktualidad.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts,Long> {

    List<Posts> findByForumTopic(ForumTopic forumTopic);

}