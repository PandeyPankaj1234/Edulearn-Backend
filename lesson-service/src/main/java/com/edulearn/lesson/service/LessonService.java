package com.edulearn.lesson.service;

import com.edulearn.lesson.dto.LessonRequest;
import com.edulearn.lesson.dto.ResourceRequest;
import com.edulearn.lesson.entity.Lesson;
import com.edulearn.lesson.entity.Resource;

import java.util.List;

public interface LessonService {

    Lesson addLesson(LessonRequest request);

    List<Lesson> getLessonsByCourse(Long courseId);

    Lesson getLessonById(Long lessonId);

    Lesson updateLesson(Long lessonId, LessonRequest request);

    void deleteLesson(Long lessonId);

    List<Lesson> reorderLessons(Long courseId, List<Long> lessonIds);

    Resource addResource(ResourceRequest request);

    void removeResource(Long resourceId);

    List<Resource> getResourcesByLesson(Long lessonId);

    List<Lesson> getPreviewLessons(Long courseId);

    long countLessons(Long courseId);
}