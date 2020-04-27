package com.gigti.xfinance.backend.others;

import com.gigti.xfinance.backend.data.InventarioActualCosto;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;

public class UtilsBackend {

    public static String generateNumberTicket(long numeroFactura){

        String numberTicket = "";
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

    public static InventarioActualCosto extractInvActCostByDate(List<InventarioActualCosto> listInvActualCosto, BigDecimal precioCosto) {
        return listInvActualCosto.stream()
                .filter(inv -> inv.getCantidad().compareTo(BigDecimal.ZERO) > 0)
                .filter(inv -> inv.getPrecioCosto().compareTo(precioCosto) == 0)
                .filter(InventarioActualCosto::isActivo)
                .findFirst()
                .orElse(null);
    }

    public static InventarioActualCosto extractInvActCostByInfiniteAndCostPrice(List<InventarioActualCosto> listInvActualCosto, BigDecimal precioCosto) {
        return listInvActualCosto.stream()
                .sorted(Comparator.comparing(InventarioActualCosto::getFechaCreacion))
                .filter(InventarioActualCosto::isInfinite)
                .filter(InventarioActualCosto::isActivo)
                .filter(inv -> inv.getPrecioCosto().compareTo(precioCosto) == 0)
                .findFirst()
                .orElse(null);
    }

    public static InventarioActualCosto extractInvActCostByDate(List<InventarioActualCosto> listInvActualCosto) {
        return listInvActualCosto.stream()
                .sorted(Comparator.comparing(InventarioActualCosto::getFechaCreacion))
                .filter(inv -> inv.getCantidad().compareTo(BigDecimal.ZERO) > 0)
                .filter(InventarioActualCosto::isActivo)
                .findFirst()
                .orElse(null);
    }

    public static InventarioActualCosto extractInvActCostByDateAndDiff(List<InventarioActualCosto> listInvActualCosto, String idDiff) {
        return listInvActualCosto.stream()
                .sorted(Comparator.comparing(InventarioActualCosto::getFechaCreacion))
                .filter(inv -> !inv.getId().equals(idDiff))
                .filter(inv -> inv.getCantidad().compareTo(BigDecimal.ZERO) > 0)
                .filter(InventarioActualCosto::isActivo)
                .findFirst()
                .orElse(null);
    }

}
