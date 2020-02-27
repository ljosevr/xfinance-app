package com.gigti.xfinance.backend.others;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String generateNumberTicket(long numeroFactura){

        String numberTicket = "";
//        numberTicket = numberTicket.concat(LocalDate.now().getYear()+"");
//        numberTicket = numberTicket.concat(LocalDate.now().getMonthValue()+"");
//        numberTicket = numberTicket.concat(LocalDate.now().getDayOfMonth()+"-");
        numberTicket = numberTicket.concat(StringUtils.leftPad(numeroFactura+"", 7,"0"));

        return numberTicket;
    }

    public static String encrytPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_512);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        byte[] encoded = Base64.encodeBase64(digest);
        return new String(encoded);
    }
}
