package com.example.DBridge2.controller;

import com.example.DBridge2.config.dto.UsersDTO;
import com.example.DBridge2.config.service.UserService;
import com.example.DBridge2.domain.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Users> signup(
            @Valid @RequestBody UsersDTO userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
//    @Secured("ROLE_ADMIN, ROLE_USER")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Users> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
//    @Secured("ROLE_ADMIN")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Users> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }
}