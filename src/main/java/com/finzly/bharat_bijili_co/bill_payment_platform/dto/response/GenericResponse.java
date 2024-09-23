package com.finzly.bharat_bijili_co.bill_payment_platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GenericResponse<T> {
    private String message;
    private T data;
}
