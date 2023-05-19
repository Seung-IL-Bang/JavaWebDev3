package com.webdev.spring.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity{

    @Id
    private String mid;

    private String mpw;
    private String email;
    private boolean del; // 탈퇴 여부
    private boolean social; // 소셜 로그인 자동 회원 가입 여부

    @ElementCollection(fetch = FetchType.LAZY) // 컬렉션을 데이터베이스 테이블에 매핑하는 데 사용 -> 해당 필드의 테이블만 따로 생성된다.
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw) {
        this.mpw = mpw;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeDel(boolean del) {
        this.del = del;
    }

    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }
}
