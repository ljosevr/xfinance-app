package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.FacturaRepository;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IventaServiceImpl implements IventaService, HasLogger {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Override
    public List<PventaDTO> find100MostImportant(Empresa empresa) {
        Pageable pageable = PageRequest.of(0, 100);
        return getListPventaDTO(empresa, pageable);
    }

    @Override
    public List<PventaDTO> findAll(Empresa empresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getListPventaDTO(empresa, pageable);
    }

    @Override
    public Factura registerSell(Usuario usuario, List<PventaDTO> listVenta) {

        //TODO Factura

        Factura factura = new Factura();
        factura.setUsuario(usuario);
        factura.setFechaCreacion(new Date());
        //factura.setNumeroFactura();
        //factura.setNumeroFacturaInterno();
        //factura.setTotalFactura();

        List<ItemFactura> listItems = new ArrayList<>();

        //Items Factura

        for(PventaDTO pv : listVenta){
            ItemFactura item = new ItemFactura();
            item.setFactura(factura);
            item.setProducto(productoRepository.getOne(pv.getIdProducto()));
            item.setCantidad(pv.getCantidadVenta());
            item.setPrecioCosto(pv.getPrecioCostoActual());
            item.setPrecioVenta(pv.getPrecioVentaActual());
            listItems.add(item);
        }

        factura.setItems(listItems);
        facturaRepository.save(factura);

        return null;
    }

    private List<PventaDTO> getListPventaDTO(Empresa empresa,Pageable pageable){
        List<Producto> list = productoRepository.findByEmpresa(empresa, pageable);
        List<PventaDTO> listDTO = new ArrayList<>();

        for(Producto p : list){
            ProductoValores pvalores = productoValoresRepository.findByProductoAndActivoIsTrue(p);
            if(pvalores != null){
                listDTO.add(convertProductoToPventaDTO(p, pvalores));
            }
        }

        return listDTO;
    }

    private PventaDTO convertProductoToPventaDTO(Producto p, ProductoValores pvalores){
        PventaDTO pv = new PventaDTO();
        pv.setIdProducto(p.getId());
        pv.setCodigoBarra(p.getCodigoBarra());
        pv.setNombreProducto(p.getNombreProducto());
        pv.setCantidadVenta(0);
        pv.setPrecioCostoActual(pvalores.getPrecioCosto());
        pv.setPrecioVentaActual(pvalores.getPrecioVenta());

        return pv;
    }

}
