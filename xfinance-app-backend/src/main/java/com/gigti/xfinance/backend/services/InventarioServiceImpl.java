package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.data.ProductoInventario;
import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.InventarioRepository;
import com.gigti.xfinance.backend.repositories.ProductoRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
public class InventarioServiceImpl implements InventarioService {

    private static final Logger logger = Logger.getLogger(InventarioServiceImpl.class.getName());

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoInventario> findAll(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findAll";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        final List<ProductoInventario> result = new ArrayList<>();
        List<Producto> listaProductos;
        if(filterText == null || filterText.isEmpty()) {
            listaProductos = productoRepository.findByEmpresa(empresa, pageable);
        } else  {
            listaProductos = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }

        listaProductos.forEach(p -> {
            List<ProductoInventario> pinventarios = inventarioRepository.findByProducto(p);
            if(CollectionUtils.isEmpty(pinventarios)) {
                ProductoInventario productoInventario = new ProductoInventario();
                productoInventario.setProducto(p);
                productoInventario.setPrecioCosto(BigDecimal.ZERO);
                productoInventario.setPrecioVenta(BigDecimal.ZERO);
                productoInventario.setQuantity(BigDecimal.ZERO);
                result.add(productoInventario);
            } else {
                AtomicBoolean existsInicial = new AtomicBoolean(false);
                pinventarios.stream()
                        .filter(ProductoInventario:: isInicial)
                        .forEach(pi -> existsInicial.set(true));

                if(existsInicial.get()) {
                    result.add(pinventarios.stream()
                            .filter(ProductoInventario:: isInicial)
                            .findFirst().get());
                } else {
                    ProductoInventario productoInventario = new ProductoInventario();
                    productoInventario.setProducto(p);
                    productoInventario.setPrecioCosto(BigDecimal.ZERO);
                    productoInventario.setPrecioVenta(BigDecimal.ZERO);
                    productoInventario.setQuantity(BigDecimal.ZERO);
                    result.add(productoInventario);
                }
            }
        });
        Collections.sort(result, (ProductoInventario o1, ProductoInventario o2) ->
                o1.getProducto().getNombreProducto().compareTo( o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName);
        return result;
    }

    @Override
    public Response saveInventarioInicial(ProductoInventario productoInventario, Usuario usuario) {
        String methodName = "findAll";
        logger.info("--> "+methodName);
        Response result = new Response();
        logger.info(productoInventario.toString());
        if(usuario != null ) {

            if(StringUtils.isBlank(productoInventario.getId())) {
                productoInventario.setUsuario(usuario);
                productoInventario.setTrackingDate(new Date());
                productoInventario.setActivo(true);
                productoInventario.setInicial(true);

                productoInventario = inventarioRepository.save(productoInventario);
                result.setSuccess(true);
                result.setMessage("Inventario Guardado Exitosamente");
                result.setObject(productoInventario);
            } else {
                result.setSuccess(false);
                result.setMessage("Inventario Inicial del producto Ya existe");
            }
        } else{
            result.setSuccess(false);
            result.setMessage("Usuario Null");
        }
        logger.info("<-- "+methodName);
        return result;
    }

}
