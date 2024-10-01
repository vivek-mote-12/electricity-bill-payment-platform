package com.finzly.bharat_bijili_co.bill_payment_platform.auth.controller;

import com.finzly.bharat_bijili_co.bill_payment_platform.auth.dto.OtpVerificationRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.auth.service.AuthService;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        String message= authService.login(username);
        return new ResponseEntity<>(new GenericResponse<>("Success",message), HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        String token= authService.verifyOtp(otpVerificationRequest);
        return new ResponseEntity<>(new GenericResponse<>("Token generated",token),HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        if (authService.validationToken(token)) {
            return new ResponseEntity<>(new GenericResponse<>("Valid token",null),HttpStatus.OK);
        }
        return new ResponseEntity<>(new GenericResponse<>("Invalid token",null),HttpStatus.UNAUTHORIZED);
    }
}
