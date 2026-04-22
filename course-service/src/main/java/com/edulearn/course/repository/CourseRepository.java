package com.edulearn.course.repository;

import com.edulearn.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategory(String category);

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByLevel(String level);

    List<Course> findByIsPublished(Boolean isPublished);

    List<Course> findByTitleContaining(String keyword);

    List<Course> findByPriceLessThanEqual(Double price);

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Course> searchByKeyword(String keyword);
}