package com.musinsa.shop.qna.service;

import com.musinsa.shop.qna.dto.AnswerRequest;
import com.musinsa.shop.qna.dto.QuestionCreateRequest;
import com.musinsa.shop.qna.dto.QuestionRead;

import java.util.List;

public interface QnAService {
    List<QuestionRead> findByItem(Integer itemId, Integer memberId, boolean isAdmin);
    List<QuestionRead> findByMember(Integer memberId);
    QuestionRead save(QuestionCreateRequest req, Integer memberId);
    QuestionRead update(Integer id, QuestionCreateRequest req, Integer memberId);
    boolean delete(Integer id, Integer memberId);
    List<QuestionRead> findAll();
    List<QuestionRead> findAllForPublic(Integer currentMemberId);
    QuestionRead answer(Integer id, AnswerRequest req, Integer adminId);
    boolean adminDelete(Integer id);
}
