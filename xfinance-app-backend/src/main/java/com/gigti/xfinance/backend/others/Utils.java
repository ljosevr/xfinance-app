package com.gigti.xfinance.backend.others;

import org.apache.commons.lang3.StringUtils;

public class Utils {

    public static String generateNumberTicket(long numeroFactura){

        String numberTicket = "";
//        numberTicket = numberTicket.concat(LocalDate.now().getYear()+"");
//        numberTicket = numberTicket.concat(LocalDate.now().getMonthValue()+"");
//        numberTicket = numberTicket.concat(LocalDate.now().getDayOfMonth()+"-");
        numberTicket = numberTicket.concat(StringUtils.leftPad(numeroFactura+"", 7,"0"));

        return numberTicket;
    }
}
