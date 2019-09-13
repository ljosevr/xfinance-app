/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.crud.Categorias;

import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.services.IcategoriaProductoService;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CategoriaDataProvider extends ListDataProvider<CategoriaProducto> {
//public class CategoriaDataProvider {

    /** Text filter that can be changed separately. */
    private String filterText = "";
    private static IcategoriaProductoService icategoriaProductoService;
    private static CategoriaDataProvider categoriaDataProvider;


    private CategoriaDataProvider() {
        super(icategoriaProductoService.findAll(CurrentUser.get().getEmpresa()));
    }

    public static CategoriaDataProvider getInstance(IcategoriaProductoService iService){
        icategoriaProductoService = iService;
        if(categoriaDataProvider == null) {
            categoriaDataProvider = new CategoriaDataProvider();
        } else{
            categoriaDataProvider.refreshAll();
        }
        return categoriaDataProvider;
    }

    /**
     * Store given Categoria to the backing data service.
     * 
     * @param categoria
     *            the updated or new Categoria
     */
    public boolean save(CategoriaProducto categoria) {
        categoria = icategoriaProductoService.guardarCategoria(categoria);
        if(categoria != null){
            refreshItem(categoria);
            return true;
        }
        return false;
    }

    /**
     * Delete given categoria from the backing data service.
     * 
     * @param categoria
     *            the CategoriaProducto to be deleted
     */
    public boolean delete(CategoriaProducto categoria) {
        if(icategoriaProductoService.eliminarCategoria(categoria.getId())){
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
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim();
        setFilter(categoriaProducto -> passesFilter(categoriaProducto.getNombre(), filterText)
                || passesFilter(categoriaProducto.getDescripcion(), filterText)
        );
    }

    @Override
    public String getId(CategoriaProducto categoria) {
        Objects.requireNonNull(categoria,
                "No se puede obtener un Id para una Categoria Null.");

        return categoria.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }

    public Collection<CategoriaProducto> findAll() {
        return icategoriaProductoService.findAll(CurrentUser.get().getEmpresa());
    }
}
