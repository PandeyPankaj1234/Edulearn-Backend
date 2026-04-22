package com.edulearn.lesson.service;

import com.edulearn.lesson.dto.LessonRequest;
import com.edulearn.lesson.dto.ResourceRequest;
import com.edulearn.lesson.entity.Lesson;
import com.edulearn.lesson.entity.Resource;
import com.edulearn.lesson.repository.LessonRepository;
import com.edulearn.lesson.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Lesson addLesson(LessonRequest request) {
        Lesson lesson = new Lesson();
        lesson.setCourseId(request.getCourseId());
        lesson.setTitle(request.getTitle());
        lesson.setContentType(request.getContentType());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setDurationMinutes(request.getDurationMinutes());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setDescription(request.getDescription());
        lesson.setIsPreview(request.getIsPreview());
        return lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndex(courseId);
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found!"));
    }

    @Override
    public Lesson updateLesson(Long lessonId, LessonRequest request) {
        Lesson lesson = getLessonById(lessonId);
        lesson.setTitle(request.getTitle());
        lesson.setContentType(request.getContentType());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setDurationMinutes(request.getDurationMinutes());
        lesson.setOrderIndex(request.getOrderIndex());
        lesson.setDescription(request.getDescription());
        lesson.setIsPreview(request.getIsPreview());
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    @Override
    public List<Lesson> reorderLessons(Long courseId, List<Long> lessonIds) {
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        for (int i = 0; i < lessonIds.size(); i++) {
            Long id = lessonIds.get(i);
            lessons.stream()
                    .filter(l -> l.getLessonId().equals(id))
                    .findFirst()
                    .ifPresent(l -> l.setOrderIndex(lessonIds.indexOf(id) + 1));
        }
        return lessonRepository.saveAll(lessons);
    }

    @Override
    public Resource addResource(ResourceRequest request) {
        Resource resource = new Resource();
        resource.setLessonId(request.getLessonId());
        resource.setName(request.getName());
        resource.setFileUrl(request.getFileUrl());
        resource.setFileType(request.getFileType());
        resource.setSizeKb(request.getSizeKb());
        return resourceRepository.save(resource);
    }

    @Override
    public void removeResource(Long resourceId) {
        resourceRepository.deleteById(resourceId);
    }

    @Override
    public List<Resource> getResourcesByLesson(Long lessonId) {
        return resourceRepository.findByLessonId(lessonId);
    }

    @Override
    public List<Lesson> getPreviewLessons(Long courseId) {
        return lessonRepository.findByCourseIdAndIsPreview(courseId, true);
    }

    @Override
    public long countLessons(Long courseId) {
        return lessonRepository.countByCourseId(courseId);
    }
}