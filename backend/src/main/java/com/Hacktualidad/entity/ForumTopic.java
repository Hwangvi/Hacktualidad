package com.Hacktualidad.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "forumTopic")
public class ForumTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="topic_id")
    private int topicId;

    @Column(length = 100, nullable = false)
    private String topicName;

    @Column(length = 2000, nullable = false)
    private String topicDescription;

    @Column(name = "background_image", length = 255)
    private String backgroundImage;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "forumTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Posts> posts;
}