package com.webdev.spring.service;

import com.webdev.spring.dto.BoardDTO;
import com.webdev.spring.dto.PageRequestDTO;
import com.webdev.spring.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("register 동작")
    public void testRegister() {

        log.info(boardService.getClass().getName()); // 변수가 가리키는 객체의 클래스명 출력

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample title...")
                .content("Sample content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    @DisplayName("modify 동작")
    public void testModify() {
        // 변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Update...title")
                .content("Update...content")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    @DisplayName("Pagination & Filter Search")
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
