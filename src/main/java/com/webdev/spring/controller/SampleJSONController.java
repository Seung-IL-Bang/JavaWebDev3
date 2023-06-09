package com.webdev.spring.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // API 서버 개발 시 사용하는 컨트롤러 애너테이션
@Hidden // Swagger API 문서화시 해당 REST 컨트롤러 제외
@Log4j2
public class SampleJSONController {

    @GetMapping("/helloArr")
    public String[] helloArr() {
        log.info("helloArr...");

        return new String[]{"AAA", "BBB", "CCC"};
    }
}
