package com.webdev.spring.repository;

import com.webdev.spring.domain.Board;
import com.webdev.spring.domain.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @DisplayName("reply insert")
    public void testInsert() {
        // 실제 DB에 있는 bno
        Long bno = 100L;

        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글...")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);
    }
}
