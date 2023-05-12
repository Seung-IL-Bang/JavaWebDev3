package com.webdev.spring.service;

import com.webdev.spring.dto.BoardDTO;
import com.webdev.spring.dto.BoardListReplyCountDTO;
import com.webdev.spring.dto.PageRequestDTO;
import com.webdev.spring.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 댓글 개수 표시 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
}
