package com.edulearn.course;

import com.edulearn.course.dto.CourseRequest;
import com.edulearn.course.entity.Course;
import com.edulearn.course.service.CourseService;
import com.edulearn.course.service.CourseServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CourseServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Course Service - JUnit Integration Tests")
class CourseServiceTest {

    @Autowired private CourseService courseService;

    private CourseRequest req(String title, String cat, Double price, Long iid) {
        CourseRequest r = new CourseRequest();
        r.setTitle(title); r.setDescription("About " + title);
        r.setCategory(cat); r.setLevel("Intermediate");
        r.setPrice(price); r.setInstructorId(iid);
        r.setLanguage("English"); r.setTotalDuration(600);
        return r;
    }

    @Test @Order(1) @DisplayName("CreateCourse: Saved with unpublished status and generated ID")
    void testCreateCourse() {
        Course c = courseService.createCourse(req("Spring Boot", "Java", 999.0, 1L));
        assertNotNull(c.getCourseId());
        assertEquals("Spring Boot", c.getTitle());
        assertFalse(c.getIsPublished());
        assertNotNull(c.getCreatedAt());
    }

    @Test @Order(2) @DisplayName("CreateCourse: Two courses get unique IDs")
    void testCreateCourse_uniqueIds() {
        Course c1 = courseService.createCourse(req("React 101", "Frontend", 499.0, 2L));
        Course c2 = courseService.createCourse(req("Node.js",   "Backend",  599.0, 2L));
        assertNotEquals(c1.getCourseId(), c2.getCourseId());
    }

    @Test @Order(3) @DisplayName("GetCourseById: Returns correct course")
    void testGetCourseById() {
        Course created = courseService.createCourse(req("Docker", "DevOps", 799.0, 3L));
        Course fetched  = courseService.getCourseById(created.getCourseId());
        assertEquals(created.getCourseId(), fetched.getCourseId());
        assertEquals("Docker", fetched.getTitle());
    }

    @Test @Order(4) @DisplayName("GetCourseById: Non-existent ID throws RuntimeException")
    void testGetCourseById_notFound() {
        assertThrows(RuntimeException.class, () -> courseService.getCourseById(99999L));
    }

    @Test @Order(5) @DisplayName("GetAllCourses: Returns all courses in the system")
    void testGetAllCourses() {
        courseService.createCourse(req("ML Intro", "AI",   1299.0, 4L));
        courseService.createCourse(req("DataViz",  "Data",  899.0, 4L));
        assertTrue(courseService.getAllCourses().size() >= 2);
    }

    @Test @Order(6) @DisplayName("GetCoursesByInstructor: Returns only that instructor's courses")
    void testGetByInstructor() {
        courseService.createCourse(req("Java 8",  "Java", 499.0, 10L));
        courseService.createCourse(req("Java 11", "Java", 599.0, 10L));
        courseService.createCourse(req("Python",  "Py",   399.0, 11L));
        List<Course> list = courseService.getCoursesByInstructor(10L);
        assertEquals(2, list.size());
        list.forEach(c -> assertEquals(10L, c.getInstructorId()));
    }

    @Test @Order(7) @DisplayName("GetCoursesByCategory: Returns only courses in given category")
    void testGetByCategory() {
        courseService.createCourse(req("AWS",   "Cloud", 1499.0, 5L));
        courseService.createCourse(req("Azure", "Cloud", 1299.0, 5L));
        courseService.createCourse(req("Terraform", "DevOps", 799.0, 6L));
        List<Course> cloud = courseService.getCoursesByCategory("Cloud");
        assertEquals(2, cloud.size());
        cloud.forEach(c -> assertEquals("Cloud", c.getCategory()));
    }

    @Test @Order(8) @DisplayName("SearchCourses: Returns courses matching keyword in title")
    void testSearchCourses() {
        courseService.createCourse(req("Kubernetes Basics",    "DevOps", 1099.0, 7L));
        courseService.createCourse(req("Advanced Kubernetes",  "DevOps", 1299.0, 7L));
        courseService.createCourse(req("Linux Basics",         "DevOps",  699.0, 7L));
        List<Course> results = courseService.searchCourses("Kubernetes");
        assertEquals(2, results.size());
    }

    @Test @Order(9) @DisplayName("UpdateCourse: Title and price updated correctly")
    void testUpdateCourse() {
        Course c = courseService.createCourse(req("GraphQL Basics", "API", 499.0, 8L));
        Course updated = courseService.updateCourse(c.getCourseId(),
                req("GraphQL Advanced", "API", 799.0, 8L));
        assertEquals("GraphQL Advanced", updated.getTitle());
        assertEquals(799.0, updated.getPrice(), 0.01);
    }

    @Test @Order(10) @DisplayName("PublishCourse: isPublished becomes true after publish")
    void testPublishCourse() {
        Course c = courseService.createCourse(req("CI/CD", "DevOps", 899.0, 9L));
        assertFalse(c.getIsPublished());
        courseService.publishCourse(c.getCourseId());
        Course published = courseService.getCourseById(c.getCourseId());
        assertTrue(published.getIsPublished());
    }

    @Test @Order(11) @DisplayName("GetPublishedCourses: Only published courses returned")
    void testGetPublishedCourses() {
        Course c1 = courseService.createCourse(req("Redis",    "Backend", 599.0, 10L));
        Course c2 = courseService.createCourse(req("RabbitMQ", "Backend", 699.0, 10L));
        courseService.publishCourse(c1.getCourseId());
        // Filter all courses to only published
        List<Course> published = courseService.getAllCourses().stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsPublished()))
                .toList();
        assertFalse(published.isEmpty(), "Should have at least one published course");
        published.forEach(c -> assertTrue(Boolean.TRUE.equals(c.getIsPublished())));
        assertTrue(published.stream().anyMatch(c -> c.getTitle().equals("Redis")));
        assertTrue(published.stream().noneMatch(c -> c.getTitle().equals("RabbitMQ")));
    }

    @Test @Order(12) @DisplayName("DeleteCourse: Deleted course throws exception on retrieval")
    void testDeleteCourse() {
        Course c = courseService.createCourse(req("ToDelete", "Test", 0.0, 99L));
        Long id = c.getCourseId();
        courseService.deleteCourse(id);
        assertThrows(RuntimeException.class, () -> courseService.getCourseById(id));
    }
}
