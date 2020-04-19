package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.data.enums.TipoMovimientoEnum;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.repositories.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class InventarioServiceImpl implements InventarioService {

    private static final Logger logger = Logger.getLogger(InventarioServiceImpl.class.getName());

    @Autowired
    private InventarioInicialRepository inventarioInicialRepository;

    @Autowired
    private InventarioActualRepository inventarioActualRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoValoresRepository productoValoresRepository;

    @Autowired
    private ImpuestoRepository impuestoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;


    @Override
    public List<InventarioInicial> findAll(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findAll";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        final List<InventarioInicial> result = new ArrayList<>();
        List<Producto> listaProductos;
        if(filterText == null || filterText.isEmpty()) {
            listaProductos = productoRepository.findByEmpresa(empresa, pageable);
        } else  {
            listaProductos = productoRepository.findByEmpresaAndNombreProducto(empresa, filterText);
        }

        listaProductos.forEach(p -> {
            InventarioInicial invInicial = inventarioInicialRepository.findByProducto(p);
            if(invInicial == null) {
                InventarioInicial inventarioInicial = new InventarioInicial();
                inventarioInicial.setProducto(p);
                inventarioInicial.setImpuesto(new Impuesto());
                inventarioInicial.setPrecioCosto(BigDecimal.ZERO);
                inventarioInicial.setPrecioVenta(BigDecimal.ZERO);
                inventarioInicial.setCantidad(BigDecimal.ZERO);
                result.add(inventarioInicial);
            } else {
                ProductoValor productoValor = productoValoresRepository.findByProductoAndActivoIsTrue(p);
                invInicial.setImpuesto(p.getImpuesto());
                invInicial.setPrecioCosto(productoValor.getPrecioCosto());
                invInicial.setPrecioVenta(productoValor.getPrecioVenta());
                result.add(invInicial);

            }
        });
        Collections.sort(result, (InventarioInicial o1, InventarioInicial o2) ->
                o1.getProducto().getNombreProducto().compareTo( o2.getProducto().getNombreProducto()));
        logger.info("<-- "+methodName);
        return result;
    }

    @Override
    public int getCount(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = productoRepository.countByEmpresaAndEliminadoIsFalse(empresa);
        } else  {
            count = productoRepository.countByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }

    @Override
    @Transactional
    public Response saveInventarioInicial(InventarioInicial inventarioInicial, Usuario usuario) {
        String methodName = "saveInventarioInicial";
        logger.info("--> "+methodName);
        Response result = new Response();
        try {
            if(usuario != null ) {

                if(StringUtils.isBlank(inventarioInicial.getId())) {
                    //Inventario Inicial
                    inventarioInicial.setUsuario(usuario);
                    inventarioInicial.setFechaActualizacion(new Date());
                    inventarioInicial = inventarioInicialRepository.save(inventarioInicial);

                    boolean process = saveProcessInventarioActualAndPrecios(inventarioInicial.getProducto(), true, inventarioInicial.getCantidad(), inventarioInicial.getPrecioVenta(), inventarioInicial.getPrecioCosto(), TipoMovimientoEnum.INV_INICIAL, true, inventarioInicial.isInfinite(),inventarioInicial.getImpuesto().getValor(), inventarioInicial.getImpuesto().getNombre());
                    if(BooleanUtils.isFalse(process)){
                        result.setSuccess(false);
                        result.setMessage("Error al Procesar Inventario Actual y Precios");
                        result.setObject(null);
                    } else {
                        result.setSuccess(true);
                        result.setMessage("Inventario Guardado Exitosamente");
                        result.setObject(inventarioInicial);
                    }
                } else {
                    result.setSuccess(false);
                    result.setMessage("Inventario Inicial del producto Ya existe");
                }
            } else{
                result.setSuccess(false);
                result.setMessage("Usuario Null");
            }
        }catch(Exception e){
            logger.log(Level.SEVERE, methodName  + ": "+e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("Error al Procesar");
        }
        logger.info("<-- "+methodName);
        return result;
    }

    @Override
    public List<InventarioActual> findInvActual(String filterText, Empresa empresa, int page, int size) {
        String methodName = "findInvActual";
        logger.info("--> "+methodName);
        Pageable pageable = PageRequest.of(page, size);
        List<InventarioActual> listResult;
        if(filterText == null || filterText.isEmpty()) {
            listResult = inventarioActualRepository.findAllByEmpresa(empresa, pageable);
        } else  {
            listResult = inventarioActualRepository.search(empresa, filterText, pageable);
        }

        return listResult;
    }

    @Override
    public int countInvActual(String filterText, Empresa empresa) {
        int count;
        if(filterText == null || filterText.isEmpty()) {
            count = inventarioActualRepository.countAllByEmpresa(empresa);
        } else  {
            count = inventarioActualRepository.countAllByEmpresaAndNombreProducto(empresa, filterText);
        }

        return count;
    }

    /**
     * Metodo para actualizar Inventario Actual, Movimientos, y ProductoValores
     * Debe aplicarse en un metodo Transactional
     * @param producto -> Es el Producto
     * @param aumentarStock -> Flag para saber si se aumentar Stock o disminuye
     * @param cantidad -> Cantidad a guardar o mover
     * @param precioVenta -> Precio Venta aplicado
     * @param precioCosto -> precio de Costo Aplicado
     * @param tipoMovimiento -> Fuente de Modificación
     * @param updatePrices -> Sirve para saber si actualiza Precios o no
     * @param infinite -> Si el producto tiene inventario infinite
     * @param impuestoValor -> Valor del impuesto a aplicar
     * @param impuestoNombre -> Nombre del Impuesto a aplicar
     * @return -> Retorna Verdadero o Falso
     */
    @Override
    public boolean saveProcessInventarioActualAndPrecios(Producto producto, boolean aumentarStock, BigDecimal cantidad, BigDecimal precioVenta, BigDecimal precioCosto, TipoMovimientoEnum tipoMovimiento, boolean updatePrices, boolean infinite, BigDecimal impuestoValor, String impuestoNombre) {
        String methodName = "saveProcessInventarioActualAndPrecios";
        logger.info("--> "+methodName);
        try {
            Date fecha = new Date();
            //Inventario Actual
            InventarioActual invActual = inventarioActualRepository.findByProducto(producto);
            if(invActual == null) {
                if(infinite) {
                    invActual = new InventarioActual(producto, BigDecimal.ZERO, infinite, fecha);
                } else {
                    invActual = new InventarioActual(producto, cantidad, infinite, fecha);
                }
            } else {
                if(!infinite) {
                    if (aumentarStock) {
                        invActual.setCantidad(invActual.getCantidad().add(cantidad));
                    } else {
                        invActual.setCantidad(invActual.getCantidad().subtract(cantidad));
                    }
                }
                invActual.setInfinite(infinite);
                invActual.setFechaActualizacion(fecha);
            }

            inventarioActualRepository.save(invActual);

            if(updatePrices) {
                //Producto Valores
                ProductoValor productoValorNew = new ProductoValor();
                ProductoValor productoValor = productoValoresRepository.findByProductoAndActivoIsTrue(producto);
                if (productoValor != null) {
                    productoValor.setActivo(false);
                    productoValoresRepository.save(productoValor);
                }
                //Nuevo ProductoValor
                productoValorNew.setActivo(true);
                productoValorNew.setFechaActualizacion(fecha);
                productoValorNew.setPrecioVenta(precioVenta);
                productoValorNew.setPrecioCosto(precioCosto);
                productoValorNew.setProducto(producto);

                productoValoresRepository.save(productoValorNew);
            }

            //Movimientos
            Movimiento movimiento = new Movimiento();
            movimiento.setProducto(producto);
            movimiento.setCantidad(cantidad);
            movimiento.setFecha(fecha);
            movimiento.setPrecioCosto(precioCosto);
            movimiento.setPrecioVenta(precioVenta);
            movimiento.setTipoMovimiento(tipoMovimiento);
            movimiento.setImpuestoValor(impuestoValor);
            movimiento.setImpuestoNombre(impuestoNombre);

            movimientoRepository.save(movimiento);

        }catch(Exception e){
            logger.log(Level.SEVERE, methodName  + ": "+e.getMessage(), e);
            return false;
        }
        logger.info("<-- "+methodName);
        return true;
    }


}