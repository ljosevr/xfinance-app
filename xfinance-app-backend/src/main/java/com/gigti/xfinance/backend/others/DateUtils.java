package com.gigti.xfinance.backend.others;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    
    /**
     * Deveuelve Null si la fecha de entrada es Null
     * @param localDateToConvert
     * @return
     */
    public static Date convertLocalDateToDate(LocalDate localDateToConvert){
        if(localDateToConvert != null){
        return java.util.Date.from(localDateToConvert.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant());
        }
        return null;
    }

    /**
     * Devuelve null si la fecha de entrada es Null
     * @param dateToConvert
     * @return
     */
    public static LocalDate convertDateToLocalDate(Date dateToConvert){
        if(dateToConvert != null){
            return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        }
        return null;
    }
}
