package com.moni.challenge.controllers;

import com.moni.challenge.domain.FundWalletRequest;
import com.moni.challenge.domain.UserRequest;
import com.moni.challenge.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }
}
