package com.example.DBridge2;

import com.example.DBridge2.config.auth.PrincipalDetailsService;
import com.example.DBridge2.domain.Users;
import com.example.DBridge2.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("1. 유저 데이터 생성하기")
    @Test
    void test_1(){
//        String encPassword = passwordEncoder.encode("test_password");
//        Users userEntity = Users.builder()
//                .id("test_user")
//                .password(encPassword)
//                .username("테스트유저")
//                .build();
//
//        Users savedUser = userRepository.save(userEntity);
//        assertThat(userEntity.getId()).isEqualTo(savedUser.getId());
    }

    @DisplayName("2. 유저정보 검색 후 비밀번호 비교")
    @Test
    void test_2(){
//        String encPassword = passwordEncoder.encode("test_password");
//
//        Users user = userRepository.findByUserId("test_user")
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//
//        assertThat(user.getPassword()).isEqualTo(encPassword);

        String userId = "test_user";
        String userPw = "test_password";
        UserDetails user = principalDetailsService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, userPw);
        authenticationManager.authenticate(authenticationToken);

        assertThat(authenticationToken.getCredentials()).isEqualTo(userPw);

        System.out.println("getCredentials: " + authenticationToken.getCredentials());
        System.out.println("userPw: " + userPw);
    }
}