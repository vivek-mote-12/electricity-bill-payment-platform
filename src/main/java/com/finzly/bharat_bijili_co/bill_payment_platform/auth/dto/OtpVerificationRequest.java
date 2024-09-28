package com.finzly.bharat_bijili_co.bill_payment_platform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OtpVerificationRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Otp cannot be blank")
    private String otp;

    private List<String> roles;
}
