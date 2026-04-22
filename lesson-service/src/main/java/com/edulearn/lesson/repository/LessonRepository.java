package com.edulearn.lesson.repository;

import com.edulearn.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseId(Long courseId);

    Optional<Lesson> findByLessonId(Long lessonId);

    List<Lesson> findByCourseIdOrderByOrderIndex(Long courseId);

    List<Lesson> findByContentType(String contentType);

    long countByCourseId(Long courseId);

    List<Lesson> findByCourseIdAndIsPreview(Long courseId, Boolean isPreview);
}