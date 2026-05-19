package com.Hacktualidad.repository;

import com.Hacktualidad.entity.ForumTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumTopicRepository extends JpaRepository<ForumTopic, Integer> {
    Optional<ForumTopic> findByTopicName(String topicName);
}
