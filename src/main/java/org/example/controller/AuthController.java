package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dto.LoginDTO;
import org.example.dto.SignupDTO;
import org.example.dto.TokenDTO;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Deprecated(since = "Registration controller was implemented")
    @Operation(hidden = true, description = "Use Registration controller")
    public ResponseEntity<TokenDTO> register(@RequestBody SignupDTO signupDTO) {
        return ResponseEntity.ok(authService.registerUser(signupDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.loginUser(loginDTO));
    }

    @Operation(hidden = true, description = "Not implemented")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
        throw new UnsupportedOperationException();
    }
}