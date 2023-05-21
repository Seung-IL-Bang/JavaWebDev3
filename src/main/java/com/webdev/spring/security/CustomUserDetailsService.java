package com.webdev.spring.security;

import com.webdev.spring.domain.Member;
import com.webdev.spring.repository.MemberRepository;
import com.webdev.spring.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 일반 로그인 처리

        log.info("loadUserByUsername: " + username);

        Optional<Member> result = memberRepository.getWithRoles(username); // 소셜 가입자는 이메일로 일반 로그인 불가능

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found ...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet()
                        .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                        .collect(Collectors.toList())
        );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}

// loadUserByUsername
// : 실제 인증을 처리할 때 호출되는 부분이다.

// passwordEncoder
// : UserDetails 를 통해 설정한 유저 정보의 패스워드를 loadUserByUsername 의 동작에서 정상적으로 자격증명에 하기 위해 필요한 객체이다.
//   만약, passwordEncoder 를 주입해주지 않으면 자격증명에 실패했다는 경고 문구가 뜰 것이다.
