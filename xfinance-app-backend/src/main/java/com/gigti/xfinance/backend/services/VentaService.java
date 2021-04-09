package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.HandledException;
import org.vaadin.data.spring.OffsetBasedPageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface VentaService {

    List<PventaDTO> findByName(String filter, Empresa empresa, int page, int size);

    PventaDTO findByBarCode(String filter, Empresa empresa);

    List<PventaDTO> findByBarCodeAndName(String filter, Empresa empresa, int page, int size);

    int count(Empresa empresa);

    Venta registrarVenta(Usuario usuario, List<PventaDTO> listVenta, Cliente cliente) throws Exception;

    boolean deleteAllVentas(Empresa emp, List<Producto> productoList);

    int count(String filter, Empresa empresa, LocalDate inicio, LocalDate fin);

    List<Venta> findAll(String filter, Empresa empresa, LocalDate inicio, LocalDate fin, OffsetBasedPageRequest offsetBasedPageRequest);
}
