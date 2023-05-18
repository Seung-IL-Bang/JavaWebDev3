package com.webdev.spring.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

        UserDetails userDetails = User.builder()
                .username("user01")
//                .password("1111") // 이렇게 사용하면 자격 증명 실패
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")
                .build();

        return userDetails;
    }
}

// loadUserByUsername
// : 실제 인증을 처리할 때 호출되는 부분이다.

// passwordEncoder
// : UserDetails 를 통해 설정한 유저 정보의 패스워드를 loadUserByUsername 의 동작에서 정상적으로 자격증명에 하기 위해 필요한 객체이다.
//   만약, passwordEncoder 를 주입해주지 않으면 자격증명에 실패했다는 경고 문구가 뜰 것이다.
