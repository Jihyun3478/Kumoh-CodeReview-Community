package com.kcr.repository;

import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeQuestionRepositoryTest {
    private final CodeQuestionRepository codeQuestionRepository;

    @Autowired
    public CodeQuestionRepositoryTest(CodeQuestionRepository codeQuestionRepository) {
        this.codeQuestionRepository = codeQuestionRepository;
    }

    @Test
    void 테스트() {
        CodeQuestion byId = codeQuestionRepository.findById(1L).get();

        System.out.println(byId.getContent());
    }
}