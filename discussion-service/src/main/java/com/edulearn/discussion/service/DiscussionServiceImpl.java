package com.edulearn.discussion.service;

import com.edulearn.discussion.dto.ReplyRequest;
import com.edulearn.discussion.dto.ThreadRequest;
import com.edulearn.discussion.entity.DiscussionThread;
import com.edulearn.discussion.entity.Reply;
import com.edulearn.discussion.repository.ReplyRepository;
import com.edulearn.discussion.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public DiscussionThread createThread(ThreadRequest request) {
        DiscussionThread thread = new DiscussionThread();
        thread.setCourseId(request.getCourseId());
        thread.setLessonId(request.getLessonId());
        thread.setAuthorId(request.getAuthorId());
        thread.setTitle(request.getTitle());
        thread.setBody(request.getBody());
        thread.setIsPinned(false);
        thread.setIsClosed(false);
        return threadRepository.save(thread);
    }

    @Override
    public Reply postReply(ReplyRequest request) {
        // Thread exist karta hai check karo
        DiscussionThread thread = threadRepository
                .findById(request.getThreadId())
                .orElseThrow(() -> new RuntimeException(
                        "Thread not found!"));

        // Closed thread mein reply nahi
        if (thread.getIsClosed()) {
            throw new RuntimeException(
                    "Thread is closed — cannot reply!");
        }

        Reply reply = new Reply();
        reply.setThreadId(request.getThreadId());
        reply.setAuthorId(request.getAuthorId());
        reply.setBody(request.getBody());
        reply.setUpvotes(0);
        reply.setIsAccepted(false);
        return replyRepository.save(reply);
    }

    @Override
    public List<DiscussionThread> getThreadsByCourse(Long courseId) {
        return threadRepository
                .findByCourseIdOrderByIsPinnedDescCreatedAtDesc(courseId);
    }

    @Override
    public List<DiscussionThread> getThreadsByLesson(Long lessonId) {
        return threadRepository.findByLessonId(lessonId);
    }

    @Override
    public List<Reply> getRepliesByThread(Long threadId) {
        return replyRepository
                .findByThreadIdOrderByUpvotesDesc(threadId);
    }

    @Override
    public void upvoteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException(
                        "Reply not found!"));
        reply.setUpvotes(reply.getUpvotes() + 1);
        replyRepository.save(reply);
    }

    @Override
    public void acceptReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException(
                        "Reply not found!"));
        reply.setIsAccepted(true);
        replyRepository.save(reply);
    }

    @Override
    public void pinThread(Long threadId) {
        DiscussionThread thread = threadRepository
                .findById(threadId)
                .orElseThrow(() -> new RuntimeException(
                        "Thread not found!"));
        thread.setIsPinned(true);
        threadRepository.save(thread);
    }

    @Override
    public void closeThread(Long threadId) {
        DiscussionThread thread = threadRepository
                .findById(threadId)
                .orElseThrow(() -> new RuntimeException(
                        "Thread not found!"));
        thread.setIsClosed(true);
        threadRepository.save(thread);
    }

    @Override
    public void deleteThread(Long threadId) {
        threadRepository.deleteById(threadId);
    }

    @Override
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    @Override
    public List<DiscussionThread> searchThreads(String keyword) {
        return threadRepository.searchByKeyword(keyword);
    }

    @Override
    public long getReplyCount(Long threadId) {
        return replyRepository.countByThreadId(threadId);
    }

    @Override
    public List<DiscussionThread> getThreadsByAuthor(Long authorId) {
        return threadRepository.findByAuthorId(authorId);
    }
}