package com.edulearn.discussion.repository;

import com.edulearn.discussion.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByThreadId(Long threadId);

    List<Reply> findByAuthorId(Long authorId);

    List<Reply> findByThreadIdOrderByUpvotesDesc(Long threadId);

    long countByThreadId(Long threadId);
}