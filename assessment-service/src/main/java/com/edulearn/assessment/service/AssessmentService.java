package com.edulearn.assessment.service;

import com.edulearn.assessment.dto.AttemptRequest;
import com.edulearn.assessment.dto.QuestionRequest;
import com.edulearn.assessment.dto.QuizRequest;
import com.edulearn.assessment.entity.Attempt;
import com.edulearn.assessment.entity.Question;
import com.edulearn.assessment.entity.Quiz;

import java.util.List;

public interface AssessmentService {

    Quiz createQuiz(QuizRequest request);

    Question addQuestion(QuestionRequest request);

    Attempt startAttempt(Long quizId, Long studentId);

    Attempt submitAttempt(AttemptRequest request);

    List<Quiz> getQuizzesByCourse(Long courseId);

    List<Question> getQuestionsByQuiz(Long quizId);

    List<Attempt> getAttemptsByStudent(Long studentId);

    List<Attempt> getAttemptsByQuiz(Long quizId);

    Quiz updateQuiz(Long quizId, QuizRequest request);

    void deleteQuiz(Long quizId);

    void publishQuiz(Long quizId);

    long getAttemptCount(Long studentId, Long quizId);
}