package com.finzly.bharat_bijili_co.bill_payment_platform.auth.controller;

import com.finzly.bharat_bijili_co.bill_payment_platform.auth.dto.OtpVerificationRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.auth.service.AuthService;
import com.finzly.bharat_bijili_co.bill_payment_platform.dto.response.GenericResponse;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.service.customer.GetCustomersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final GetCustomersService getCustomersService;

    public AuthController(AuthService authService, GetCustomersService getCustomersService){
        this.authService=authService;
        this.getCustomersService = getCustomersService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAndLogin(@RequestParam String username) {
        Customer customer = getCustomersService.getCustomerByEmail(username);
        if(customer == null){
            return new ResponseEntity<>(new GenericResponse<>("Email does not exist",null),
                    HttpStatus.UNAUTHORIZED);
        }
        String message= authService.login(username);
        return new ResponseEntity<>(new GenericResponse<>("Success",message), HttpStatus.OK);
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

    @GetMapping("/get-customer")
    public ResponseEntity<?> getCustomerByEmail(@RequestParam String email) {
        Customer customer = getCustomersService.getCustomerByEmail(email);
        return new ResponseEntity<>(new GenericResponse<>("Success",customer),HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        if (authService.validationToken(token)) {
            return new ResponseEntity<>(new GenericResponse<>("Valid token",null),HttpStatus.OK);
        }
        return new ResponseEntity<>(new GenericResponse<>("Invalid token",null),HttpStatus.UNAUTHORIZED);
    }
}
