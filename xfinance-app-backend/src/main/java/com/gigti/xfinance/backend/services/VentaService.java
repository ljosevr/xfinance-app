package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Factura;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.PventaDTO;

import java.util.List;

public interface VentaService {

    public List<PventaDTO> find100MostImportant(Empresa empresa);

    public List<PventaDTO> findAll(Empresa empresa, int page, int size);

    public Factura registrarFactura(Usuario usuario, List<PventaDTO> listVenta);

}
