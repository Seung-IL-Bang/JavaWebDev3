package com.webdev.spring.service;

import com.webdev.spring.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
                .bno(102L)
                .title("Update...title")
                .content("Update...content")
                .build();

        // 첨부파일 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID().toString() + "_zzz.jpg"));

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

    @Test
    public void testRegisterWithImages() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_" + "aaa.jpg",
                        UUID.randomUUID() + "_" + "bbb.jpg",
                        UUID.randomUUID() + "_" + "ccc.jpg"
                ));

        Long bno = boardService.register(boardDTO);

        log.info(bno);
    }

    @Test
    public void testReadAll() {
        Long bno = 102L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        } // end for
    }

    @Test
    public void testRemoveAll() { // 특정 게시글에 댓글이 없는 경우
        Long bno = 1L;

        boardService.remove(bno);
    }

    @Test
    public void testListWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + "_" + boardListAllDTO.getTitle());

            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()) {
                    log.info(boardImageDTO);
                }
            }
            log.info("------------------------");
        });
    }
}
