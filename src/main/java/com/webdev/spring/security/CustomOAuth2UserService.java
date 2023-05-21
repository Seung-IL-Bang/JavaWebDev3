package com.webdev.spring.security;

import com.webdev.spring.domain.Member;
import com.webdev.spring.domain.MemberRole;
import com.webdev.spring.repository.MemberRepository;
import com.webdev.spring.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // 소셜 로그인 처리

        log.info("userRequest...");
        log.info(userRequest);
        log.info("oauth2 user.........................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

//        paramMap.forEach((k, v) -> {
//            log.info("------------OAuth2 User Attributes-------------");
//            log.info(k + ":" + v);
//        });

        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("=================================");
        log.info(email);
        log.info("=================================");

        return generateDTO(email, paramMap);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params) {

        Optional<Member> result = memberRepository.findByEmail(email);

        // 데이터베이스에 해당 이메일을 가진 사용자가 없다면
        if (result.isEmpty()) {
            // 회원 추가 -- mid는 이메일 주소, mpw는 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO
                    = new MemberSecurityDTO(email, "1111", email, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        } else {
            Member member = result.get();

            MemberSecurityDTO memberSecurityDTO
                    = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet()
                            .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                            .collect(Collectors.toList()));

            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("KAKAO---------------------------------");

        Object kakao_account = paramMap.get("kakao_account");

        log.info(kakao_account);

        LinkedHashMap accountMap = (LinkedHashMap) kakao_account;

        String email = (String) accountMap.get("email");

        log.info("email : " + email);

        return email;
    }


}
