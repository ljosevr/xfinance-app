package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.Venta;
import com.gigti.xfinance.backend.data.dto.PventaDTO;

import java.util.List;

public interface VentaService {

    List<PventaDTO> findByName(String filter, Empresa empresa, int page, int size);

    PventaDTO findByBarCode(String filter, Empresa empresa);

    List<PventaDTO> findByBarCodeAndName(String filter, Empresa empresa, int page, int size);

    int count(Empresa empresa);

    Venta registrarVenta(Usuario usuario, List<PventaDTO> listVenta);

    boolean deleteAllVentas(Empresa emp, List<Producto> productoList);
}
