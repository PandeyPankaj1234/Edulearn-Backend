package com.edulearn.assessment.repository;

import com.edulearn.assessment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuizIdOrderByOrderIndex(Long quizId);

    void deleteByQuizId(Long quizId);

    long countByQuizId(Long quizId);
}