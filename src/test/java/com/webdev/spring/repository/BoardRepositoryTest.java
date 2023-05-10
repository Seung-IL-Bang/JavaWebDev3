package com.webdev.spring.repository;

import com.webdev.spring.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

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
}
