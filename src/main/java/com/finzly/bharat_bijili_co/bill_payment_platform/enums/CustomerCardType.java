package com.finzly.bharat_bijili_co.bill_payment_platform.enums;

public enum CustomerCardType {
    CREDIT_CARD("CREDIT_CARD"),
    DEBIT_CARD("DEBIT_CARD");

    private String value;

    CustomerCardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
