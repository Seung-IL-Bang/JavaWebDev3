package com.webdev.spring.repository;

import com.webdev.spring.domain.Board;
import com.webdev.spring.domain.BoardImage;
import com.webdev.spring.dto.BoardListAllDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @DisplayName("save 동작")
    public void testInsert() {

        IntStream.rangeClosed(1,100).forEach(i ->{
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user..." + (i % 10))
                    .build();

            Board result = boardRepository.save(board);

            log.info("BNO: " + result.getBno());
        });
    }

    @Test
    @DisplayName("select 동작")
    public void testSelect() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    @DisplayName("update 동작")
    public void testUpdate() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update...title", "update...content");

        boardRepository.save(board);
    }

    @Test
    @DisplayName("delete 동작")
    public void testDelete() {
        Long bno = 102L;

        boardRepository.deleteById(bno);
    }

    @Test
    @DisplayName("Pagination 동작")
    public void testPaging() {
        // 1 page order by dno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(log::info);
    }

    @Test
    @DisplayName("search1 동작")
    public void testSearch1() {
        // 2 page order by bno desc
        Pageable pagealbe = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pagealbe);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // total pages
        log.info(result.getTotalPages());

        // page size
        log.info(result.getSize());

        // pageNumber
        log.info(result.getNumber());

        // prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(log::info);
    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional // no session 에러 방지를 위한 트랜잭션 추가
    public void testReadWithImages() {
        // 반드시 존재하는 bno 로 확인
        Optional<Board> result = boardRepository.findById(1L); // 첫 번째 select

        Board board = result.orElseThrow();

        log.info(board);
        log.info("---------------");
        log.info(board.getImageSet()); // 두 번째 select
    }

    // @EntityGraph 를 통해 Board 와 함께 로딩할 속성(BoardImage)을 명시 -> Join 처리를 통해 한 번의 select 만 이루어짐
    @Test
    public void testReadWithImages2() {
        // 반드시 존재하는 bno 로 확인
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("---------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }
    }

    @Test
    @Commit
    @Transactional
    public void testModifyImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        // 기존의 첨부파일들 삭제
        board.clearImages();

        // 새로운 첨부파일들 추가
        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll() {
        for (int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("Title_" + i)
                    .content("Content_" + i)
                    .writer("Writer_" + i)
                    .build();

            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), + i + "_file_" + j + ".jpg");
            }

            boardRepository.save(board);
        }
    }

    @Test
    @Transactional
    public void testSearchImageReplyCount() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //boardRepository.searchWithAll(null, null, pageable);

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("----------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
    }
}
