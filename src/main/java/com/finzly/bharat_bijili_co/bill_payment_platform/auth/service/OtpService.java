package com.finzly.bharat_bijili_co.bill_payment_platform.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private Map<String, String> otpStorage = new HashMap<>();

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final long OTP_EXPIRATION_MINUTES = 5;

    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void storeOtp(String username, String otp) {
        redisTemplate.opsForValue().set(getOtpKey(username), otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public String getStoredOtp(String username) {
        return redisTemplate.opsForValue().get(getOtpKey(username));
    }

    public void clearOtp(String username) {
        redisTemplate.delete(getOtpKey(username));
    }

    private String getOtpKey(String username) {
        return "OTP:" + username;
    }
}
