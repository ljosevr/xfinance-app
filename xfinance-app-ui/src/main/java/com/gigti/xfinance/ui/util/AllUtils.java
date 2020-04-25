package com.gigti.xfinance.ui.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class AllUtils {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public static BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(ONE_HUNDRED);
    }

    public static String numberFormat(BigDecimal value){
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setCurrency(Currency.getInstance(Locale.US));
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(0);

        return "$ "+decimalFormat.format(value);
    }

    public static String formatUnidadMedida(String medida){
        switch (medida) {
            case "UNIDAD":
                return "ud";
            case "PAQUETE":
                return "PQ";
            case "KILO":
                return "kg";
            case "LIBRA":
                return "lb";
            case "LITRO":
                return "l";
            case "GRAMO":
                return "g";
            case "MILI_LITRO":
                return "ml";
            case "CENTIMETRO":
                return "cm";
        }
        return "N/A";
    }

    public static String formatDate(Date fecha){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return fecha != null ? df.format(fecha) : "";
    }
}
