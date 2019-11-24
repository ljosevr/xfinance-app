package com.gigti.xfinance.backend.others;

import com.gigti.xfinance.backend.data.Empresa;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class Utils {

    public static String generateNumberTicket(long numeroEmpresa, long numeroFactura){

        String numberTicket = numeroEmpresa+"-";
        numberTicket = numberTicket.concat(LocalDate.now().getYear()+"");
        numberTicket = numberTicket.concat(LocalDate.now().getMonthValue()+"");
        numberTicket = numberTicket.concat(LocalDate.now().getDayOfMonth()+"-");
        numberTicket = numberTicket.concat(StringUtils.leftPad(numeroFactura+"", 6,"0"));

        return numberTicket;
    }
}
