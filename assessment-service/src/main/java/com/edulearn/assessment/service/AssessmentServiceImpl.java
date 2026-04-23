package com.edulearn.assessment.service;

import com.edulearn.assessment.dto.AttemptRequest;
import com.edulearn.assessment.dto.QuestionRequest;
import com.edulearn.assessment.dto.QuizRequest;
import com.edulearn.assessment.entity.Attempt;
import com.edulearn.assessment.entity.Question;
import com.edulearn.assessment.entity.Quiz;
import com.edulearn.assessment.repository.AttemptRepository;
import com.edulearn.assessment.repository.QuestionRepository;
import com.edulearn.assessment.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    @Override
    public Quiz createQuiz(QuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setCourseId(request.getCourseId());
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setPassingScore(request.getPassingScore());
        quiz.setMaxAttempts(request.getMaxAttempts());
        quiz.setIsPublished(false);
        return quizRepository.save(quiz);
    }

    @Override
    public Question addQuestion(QuestionRequest request) {
        Question question = new Question();
        question.setQuizId(request.getQuizId());
        question.setText(request.getText());
        question.setType(request.getType());
        question.setOptions(request.getOptions());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setMarks(request.getMarks());
        question.setOrderIndex(request.getOrderIndex());
        return questionRepository.save(question);
    }

    @Override
    public Attempt startAttempt(Long quizId, Long studentId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found!"));

        // Max attempts check
        long attemptCount = attemptRepository
                .countByStudentIdAndQuizId(studentId, quizId);
        if (quiz.getMaxAttempts() != null &&
                attemptCount >= quiz.getMaxAttempts()) {
            throw new RuntimeException("Max attempts reached!");
        }

        Attempt attempt = new Attempt();
        attempt.setQuizId(quizId);
        attempt.setStudentId(studentId);
        attempt.setStartedAt(LocalDateTime.now());
        return attemptRepository.save(attempt);
    }

    @Override
    public Attempt submitAttempt(AttemptRequest request) {
        // Questions fetch karo
        List<Question> questions = questionRepository
                .findByQuizIdOrderByOrderIndex(request.getQuizId());

        // Auto grade karo
        int totalMarks = 0;
        int earnedMarks = 0;
        Map<Long, String> answers = request.getAnswers();

        for (Question q : questions) {
            totalMarks += (q.getMarks() != null ? q.getMarks() : 1);
            String studentAnswer = answers.get(q.getQuestionId());
            if (studentAnswer != null &&
                    studentAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                earnedMarks += (q.getMarks() != null ? q.getMarks() : 1);
            }
        }

        // Score calculate karo
        int scorePercent = totalMarks > 0 ?
                (earnedMarks * 100) / totalMarks : 0;

        // Quiz passing score check
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found!"));
        boolean passed = scorePercent >= (quiz.getPassingScore() != null ?
                quiz.getPassingScore() : 50);

        // Attempt save karo
        Attempt attempt = new Attempt();
        attempt.setQuizId(request.getQuizId());
        attempt.setStudentId(request.getStudentId());
        attempt.setAnswers(request.getAnswers());
        attempt.setScore(scorePercent);
        attempt.setPassed(passed);
        attempt.setSubmittedAt(LocalDateTime.now());

        return attemptRepository.save(attempt);
    }

    @Override
    public List<Quiz> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId);
    }

    @Override
    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizIdOrderByOrderIndex(quizId);
    }

    @Override
    public List<Attempt> getAttemptsByStudent(Long studentId) {
        return attemptRepository.findByStudentId(studentId);
    }

    @Override
    public List<Attempt> getAttemptsByQuiz(Long quizId) {
        return attemptRepository.findByQuizId(quizId);
    }

    @Override
    public Quiz updateQuiz(Long quizId, QuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found!"));
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setPassingScore(request.getPassingScore());
        quiz.setMaxAttempts(request.getMaxAttempts());
        return quizRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    @Override
    public void publishQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found!"));
        quiz.setIsPublished(true);
        quizRepository.save(quiz);
    }

    @Override
    public long getAttemptCount(Long studentId, Long quizId) {
        return attemptRepository.countByStudentIdAndQuizId(studentId, quizId);
    }
}