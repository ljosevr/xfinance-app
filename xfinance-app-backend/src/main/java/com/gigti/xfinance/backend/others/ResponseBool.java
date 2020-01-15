package com.gigti.xfinance.backend.others;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBool {

    private boolean sucess;
    private String code;
    private String msg;

}
