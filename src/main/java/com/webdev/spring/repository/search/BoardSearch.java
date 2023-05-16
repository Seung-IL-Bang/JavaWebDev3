package com.webdev.spring.repository.search;

import com.webdev.spring.domain.Board;
import com.webdev.spring.dto.BoardListAllDTO;
import com.webdev.spring.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

}
