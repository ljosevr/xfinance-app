package com.gigti.xfinance.backend.others;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private boolean success;
    private String code;
    private String message;
    private Object object;

}
