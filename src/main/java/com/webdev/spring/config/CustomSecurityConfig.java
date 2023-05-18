package com.webdev.spring.config;

import com.webdev.spring.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final DataSource dataSource;
    @Lazy private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("--------------------------------Configure--------------------------------");

        http.formLogin() // 로그인 화면에서 로그인을 진행한다는 설정 (로그인 화면 활성화)
                .loginPage("/member/login"); // formLogin() 에 대해 loginPage() 를 지정하면 로그인이 필요한 경우에 원하는 경로로 자동 리다이렉트 된다. (기본 로그인 화면 비활성화)

        http.csrf().disable(); // CSRF 토큰 비활성화

        // 자동 로그인 remember-me 기능
        http.rememberMe()
                .key("12345678") // 쿠키의 값을 인코딩하기 위한 키
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60*60*24*30); // 30일 유효기간


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("--------------------------------Web Configure--------------------------------");

        return (web -> web.ignoring().requestMatchers(
                        PathRequest
                                .toStaticResources()
                                .atCommonLocations()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 해시 알고리즘으로 패스워드 암호화; 같은 문자열이라도 매번 해시 처리된 결과가 다르다.
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

}

// filterChain
// : 해당 메소드가 적용되면 Spring Web Security 가 기본으로 제공하는 모든 자원에 대한 로그인 제한이 사라진다. (로그인 화면 비활성화)
//  참고로, Spring Web Security 는 기본적으로 고정적인 아이디 'user' 와 프로젝트를 실행할 때마다 매번 다른 패스워드를 콘솔에 띄운다.
//  filterChain 에 formLogin() 을 적용하면, 다시 로그인 화면이 활성화 되는데, 이 경우엔 아이디와 패스워드를 제공해주지 않기 때문에 UserDetails 객체의 설정 등을 통해 별도로 정해줘야 한다.
//  또한 filterChain 이 적용된 이상 모든 자원에 대한 권한 설정이 없기 때문에 권한 설정도 따로 진행해야 한다.(formLogin 으로 로그인 화면이 활성화 되더라도 다른 자원에 접근 가능함.)
//  로그인 성공 시에는 '/' 기본 루트로 리다이렉트 된다.

// webSecurityCustomizer
// : 정적 자원에 대해서는 Security 필터 처리를 진행하지 않는 설정이다. (필터 제외 처리)

// passwordEncoder()
// : UserDetailsService 가 정상적으로 동작하려면 SecurityConfig 클래스에 PasswordEncoder 를 @Bean 으로 지정하고 주입해줘야 한다.