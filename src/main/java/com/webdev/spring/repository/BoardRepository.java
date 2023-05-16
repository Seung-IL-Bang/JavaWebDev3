package com.webdev.spring.repository;

import com.webdev.spring.domain.Board;
import com.webdev.spring.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);

    @Query("select b from Board b where b.title like concat('%', :keyword, '%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);

    @Query(value = "select now()", nativeQuery = true) // 특정 DB 에서 동작하는 SQL 로 사용 가능
    String getTime();

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

}
