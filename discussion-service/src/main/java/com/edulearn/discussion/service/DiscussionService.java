package com.edulearn.discussion.service;

import com.edulearn.discussion.dto.ReplyRequest;
import com.edulearn.discussion.dto.ThreadRequest;
import com.edulearn.discussion.entity.DiscussionThread;
import com.edulearn.discussion.entity.Reply;

import java.util.List;

public interface DiscussionService {

    DiscussionThread createThread(ThreadRequest request);

    Reply postReply(ReplyRequest request);

    List<DiscussionThread> getThreadsByCourse(Long courseId);

    List<DiscussionThread> getThreadsByLesson(Long lessonId);

    List<Reply> getRepliesByThread(Long threadId);

    void upvoteReply(Long replyId);

    void acceptReply(Long replyId);

    void pinThread(Long threadId);

    void closeThread(Long threadId);

    void deleteThread(Long threadId);

    void deleteReply(Long replyId);

    List<DiscussionThread> searchThreads(String keyword);

    long getReplyCount(Long threadId);
}