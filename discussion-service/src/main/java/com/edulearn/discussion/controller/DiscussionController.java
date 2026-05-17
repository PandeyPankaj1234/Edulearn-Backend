package com.edulearn.discussion.controller;

import com.edulearn.discussion.dto.ReplyRequest;
import com.edulearn.discussion.dto.ThreadRequest;
import com.edulearn.discussion.entity.DiscussionThread;
import com.edulearn.discussion.entity.Reply;
import com.edulearn.discussion.service.DiscussionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")

public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    // POST /api/discussions/threads
    @PostMapping("/threads")
    public ResponseEntity<DiscussionThread> createThread(
            @Valid @RequestBody ThreadRequest request) {
        return ResponseEntity.ok(
                discussionService.createThread(request));
    }

    // GET /api/discussions/threads/course/{courseId}
    @GetMapping("/threads/course/{courseId}")
    public ResponseEntity<List<DiscussionThread>> getThreadsByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                discussionService.getThreadsByCourse(courseId));
    }

    // GET /api/discussions/threads/lesson/{lessonId}
    @GetMapping("/threads/lesson/{lessonId}")
    public ResponseEntity<List<DiscussionThread>> getThreadsByLesson(
            @PathVariable Long lessonId) {
        return ResponseEntity.ok(
                discussionService.getThreadsByLesson(lessonId));
    }

    // GET /api/discussions/threads/author/{authorId}
    @GetMapping("/threads/author/{authorId}")
    public ResponseEntity<List<DiscussionThread>> getThreadsByAuthor(
            @PathVariable Long authorId) {
        return ResponseEntity.ok(
                discussionService.getThreadsByAuthor(authorId));
    }

    // GET /api/discussions/threads/search?keyword=java
    @GetMapping("/threads/search")
    public ResponseEntity<List<DiscussionThread>> searchThreads(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                discussionService.searchThreads(keyword));
    }

    // PUT /api/discussions/threads/{threadId}/pin
    @PutMapping("/threads/{threadId}/pin")
    public ResponseEntity<String> pinThread(
            @PathVariable Long threadId) {
        discussionService.pinThread(threadId);
        return ResponseEntity.ok("Thread pinned successfully!");
    }

    // PUT /api/discussions/threads/{threadId}/close
    @PutMapping("/threads/{threadId}/close")
    public ResponseEntity<String> closeThread(
            @PathVariable Long threadId) {
        discussionService.closeThread(threadId);
        return ResponseEntity.ok("Thread closed successfully!");
    }

    // DELETE /api/discussions/threads/{threadId}
    @DeleteMapping("/threads/{threadId}")
    public ResponseEntity<String> deleteThread(
            @PathVariable Long threadId) {
        discussionService.deleteThread(threadId);
        return ResponseEntity.ok("Thread deleted successfully!");
    }

    // POST /api/discussions/replies
    @PostMapping("/replies")
    public ResponseEntity<Reply> postReply(
            @Valid @RequestBody ReplyRequest request) {
        return ResponseEntity.ok(
                discussionService.postReply(request));
    }

    // GET /api/discussions/replies/thread/{threadId}
    @GetMapping("/replies/thread/{threadId}")
    public ResponseEntity<List<Reply>> getRepliesByThread(
            @PathVariable Long threadId) {
        return ResponseEntity.ok(
                discussionService.getRepliesByThread(threadId));
    }

    // PUT /api/discussions/replies/{replyId}/upvote
    @PutMapping("/replies/{replyId}/upvote")
    public ResponseEntity<String> upvoteReply(
            @PathVariable Long replyId) {
        discussionService.upvoteReply(replyId);
        return ResponseEntity.ok("Reply upvoted successfully!");
    }

    // PUT /api/discussions/replies/{replyId}/accept
    @PutMapping("/replies/{replyId}/accept")
    public ResponseEntity<String> acceptReply(
            @PathVariable Long replyId) {
        discussionService.acceptReply(replyId);
        return ResponseEntity.ok("Reply accepted as best answer!");
    }

    // DELETE /api/discussions/replies/{replyId}
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<String> deleteReply(
            @PathVariable Long replyId) {
        discussionService.deleteReply(replyId);
        return ResponseEntity.ok("Reply deleted successfully!");
    }

    // GET /api/discussions/replies/thread/{threadId}/count
    @GetMapping("/replies/thread/{threadId}/count")
    public ResponseEntity<Long> getReplyCount(
            @PathVariable Long threadId) {
        return ResponseEntity.ok(
                discussionService.getReplyCount(threadId));
    }
}