package com.webdev.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne // bidirectional
    private Board board;


    @Override
    public int compareTo(BoardImage other) { // @OneToMany 처리에서 순번에 맞게 정렬하기 위해 사용
        return this.ord - other.ord;
    }

    public void changeBoard(Board board) { // BoardImage 객체의 참조 변경 시 사용
        this.board = board;
    }
}
