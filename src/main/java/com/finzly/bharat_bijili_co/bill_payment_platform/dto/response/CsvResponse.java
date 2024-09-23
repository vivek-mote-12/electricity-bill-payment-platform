package com.finzly.bharat_bijili_co.bill_payment_platform.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvResponse {
    private String message;
    private int successfulCount;
    private int failedCount;
    private List<String> failedRecords;
}
