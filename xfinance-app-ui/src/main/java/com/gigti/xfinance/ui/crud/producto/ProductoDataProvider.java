/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

//@SpringComponent
public class ProductoDataProvider extends ListDataProvider<Producto> {

    /** Text filter that can be changed separately. */
    private String filterText = "";
    private static ProductoDataProvider productoDataProvider;
    private static IProductoService iProductoService;
    private static Empresa empresa;

    private ProductoDataProvider() {
        //TODO Manejar paginador desde el Inicio
        super(iProductoService.findAll(empresa));
    }

    public static ProductoDataProvider getInstance(IProductoService iServiceProd){
        iProductoService = iServiceProd;
        empresa  = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
        if(productoDataProvider == null){
            productoDataProvider = new ProductoDataProvider();
        }else{
            productoDataProvider.refreshAll();
        }
        return productoDataProvider;
    }

    /**
     * Store given product to the backing data service.
     * 
     * @param producto the updated or new product
     */
    public boolean save(Producto producto) {
        producto.setEmpresa(empresa);
        producto = iProductoService.saveProduct(producto);
        if(producto != null){
            refreshItem(producto);
            return true;
        }
        return false;
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param producto the product to be deleted
     */
    public boolean delete(Producto producto) {
        if(iProductoService.delete(producto.getId())){
            refreshAll();
            return true;
        }
        return false;
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for product name, availability and category.
     * 
     * @param filterText
     *            the text to filter by, never null
     */
    public List<Producto> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            productoDataProvider.refreshAll();
            return null;
        }
        this.filterText = filterText.trim();
        return iProductoService.findByNombreProducto(empresa, filterText);
    }

    @Override
    public String getId(Producto product) {
        Objects.requireNonNull(product,
                "No se puede obtener un Id para un Producto Null");

        return product.getId();
    }

    public Collection<Producto> findAll() {
        return iProductoService.findAll(empresa);
    }

    public Producto findById(String productoId) {
        return iProductoService.findById(productoId);
    }
}
