/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.enums.TipoMedidaEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.InventarioInicialRepository;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import com.gigti.xfinance.backend.repositories.ProductoValoresRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    Logger logger = LoggerFactory.getLogger(InitBackServiceImpl.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Transactional
    public Producto saveProduct(Producto producto, Usuario usuario) {
        return productoRepository.save(producto);
    }

    @Transactional
    public Response delete(String id) {
        Response response = new Response();
        try {
            Producto producto = productoRepository.findById(id).orElse(null);
            if (producto != null) {
                producto.setEliminado(true);
                productoRepository.save(producto);
                response.setMessage("Producto Eliminado");
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
                response.setMessage("Producto NO encontrado");
            }
        } catch (Exception e) {
            logger.debug("Error: " + e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Error al Eliminar Producto: "+e.getMessage());
        }
        return response;
    }

    public List<Producto> findByNombreProducto(Empresa empresa, String productName) {
        return productoRepository.findByEmpresaAndNombreProducto(empresa, productName);
    }

    public List<Producto> findAll(String filterText, Empresa empresa, int page, int size) {
        logger.info("--> findAll Productos: "+page + " - "+size);
        Pageable pageable = PageRequest.of(page, size);
        List<Producto> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = productoRepository.findByEmpresa(empresa, pageable);
        } else  {
            listResult = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }
        return listResult;
    }

    public List<Producto> findAll(Empresa empresa) {
        return productoRepository.findByEmpresa(empresa);
    }

    public Producto findByBarCode(Empresa empresa, String barCode) {
        return productoRepository.findByEmpresaAndCodigoBarra(empresa, barCode);
    }

    public List<Producto> findByNameOrBarCode(Empresa empresa, String anything) {
        return productoRepository.findByEmpresaAndNombreProductoContainingOrCodigoBarraContaining(empresa, anything, anything);
    }

    @Override
    public Producto findById(String id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<TipoMedidaEnum> getAllTipoMedidaEnum(){
        List<TipoMedidaEnum> lista = new ArrayList<>();
        lista.add(TipoMedidaEnum.UNIDAD);
        lista.add(TipoMedidaEnum.PAQUETE);
        lista.add(TipoMedidaEnum.KILO);
        lista.add(TipoMedidaEnum.LIBRA);

        return lista;
    }

    @Override
    public int count(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = productoRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = productoRepository.countByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }
}
