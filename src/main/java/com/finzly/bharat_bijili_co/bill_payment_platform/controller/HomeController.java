package com.finzly.bharat_bijili_co.bill_payment_platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("")
    public String getHello(){
        return "Hello world";
    }
}
