package com.gigti.xfinance.backend.services;

import java.time.LocalDate;
import java.util.List;

import com.gigti.xfinance.backend.data.Cliente;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.data.dto.PventaDTO;

import org.vaadin.data.spring.OffsetBasedPageRequest;

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
