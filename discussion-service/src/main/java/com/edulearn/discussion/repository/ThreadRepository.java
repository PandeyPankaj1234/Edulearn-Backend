package com.edulearn.discussion.repository;

import com.edulearn.discussion.entity.DiscussionThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends
        JpaRepository<DiscussionThread, Long> {

    List<DiscussionThread> findByCourseId(Long courseId);

    List<DiscussionThread> findByLessonId(Long lessonId);

    List<DiscussionThread> findByAuthorId(Long authorId);

    List<DiscussionThread> findByIsPinned(Boolean isPinned);

    @Query("SELECT t FROM DiscussionThread t WHERE " +
            "t.title LIKE %:keyword% OR t.body LIKE %:keyword%")
    List<DiscussionThread> searchByKeyword(String keyword);

    List<DiscussionThread> findByCourseIdOrderByIsPinnedDescCreatedAtDesc(
            Long courseId);
}