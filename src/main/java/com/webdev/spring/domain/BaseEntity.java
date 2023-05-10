package com.webdev.spring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "reqDate", updatable = false)
    private LocalDateTime reqDate;

    @LastModifiedDate
    @Column(name = "modDate")
    private LocalDateTime modDate;
}

// @MappedSuperclass
// : 공통으로 사용되는 속성들을 모아 해당 클래스에 선언하고, 해당 클래스를 상속받은 하위 클래스들은 공통 속성들을 쉽게 생성할 수 있도록 해준다.
