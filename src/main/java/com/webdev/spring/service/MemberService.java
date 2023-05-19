package com.webdev.spring.service;

import com.webdev.spring.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
