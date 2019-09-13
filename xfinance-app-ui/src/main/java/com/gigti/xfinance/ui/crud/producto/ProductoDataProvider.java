/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.producto;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.Producto;
import com.gigti.xfinance.backend.services.IProductoService;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Objects;

public class ProductoDataProvider extends ListDataProvider<Producto> {

    /** Text filter that can be changed separately. */
    private String filterText = "";

    private static ProductoDataProvider productoDataProvider;

    @Autowired
    private static IProductoService iProductoService;

    private ProductoDataProvider() {
        //TODO Manejar paginador desde el Inicio
        super(iProductoService.findAll(new Empresa()));
    }

    public static ProductoDataProvider getInstance(){
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
     * @param product
     *            the updated or new product
     */
    public void save(Producto product) {
        //boolean newProduct = product.isNewProduct();

        iProductoService.saveProduct(product);
        refreshItem(product);
//        if (newProduct) {
//            refreshAll();
//        } else {
//            refreshItem(product);
//        }
    }

    /**
     * Delete given product from the backing data service.
     * 
     * @param product
     *            the product to be deleted
     */
    public void delete(Producto product) {
        //DataService.get().deleteProduct(product.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for product name, availability and category.
     * 
     * @param filterText
     *            the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();
        setFilter(producto -> passesFilter(producto.getNombreProducto(), filterText)
                //|| passesFilter(product.getAvailability(), filterText)
                //|| passesFilter(producto.getCategoria().getNombre(), filterText));
        );
    }

    @Override
    public String getId(Producto product) {
        Objects.requireNonNull(product,
                "Cannot provide an id for a null product.");

        return product.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }

}
