package com.webdev.spring.service;

import com.webdev.spring.dto.PageRequestDTO;
import com.webdev.spring.dto.PageResponseDTO;
import com.webdev.spring.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
