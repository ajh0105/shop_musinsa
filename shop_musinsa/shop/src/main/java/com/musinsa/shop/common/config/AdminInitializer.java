package com.musinsa.shop.common.config;

import com.musinsa.shop.member.entity.Member;
import com.musinsa.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1) 관리자 계정 생성
        if (!memberRepository.existsByLoginId("admin@musinsa.com")) {
            Member admin = Member.builder()
                    .name("관리자")
                    .loginId("admin@musinsa.com")
                    .loginPw(passwordEncoder.encode("admin1234"))
                    .role("ROLE_ADMIN")
                    .grade("VIP")
                    .status("ACTIVE")
                    .build();
            memberRepository.save(admin);
            log.info("✅ 관리자 계정 생성 완료: admin@musinsa.com / admin1234");
        }

        // 2) 기존 평문 패스워드 BCrypt 마이그레이션
        memberRepository.findAll().forEach(member -> {
            String pw = member.getLoginPw();
            if (pw != null && !pw.startsWith("$2a$") && !pw.startsWith("$2b$")) {
                member.setLoginPw(passwordEncoder.encode(pw));
                memberRepository.save(member);
                log.info("🔐 패스워드 암호화: {}", member.getLoginId());
            }
        });
    }
}
