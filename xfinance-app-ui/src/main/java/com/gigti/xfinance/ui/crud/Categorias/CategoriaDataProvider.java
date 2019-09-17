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

    /** Text filter that can be changed separately. */
    private String filterText = "";
    private static IcategoriaProductoService icategoriaProductoService;
    private static CategoriaDataProvider categoriaDataProvider;
    private static Empresa empresa;


    private CategoriaDataProvider() {
        super(icategoriaProductoService.findAll(empresa));
    }

    public static CategoriaDataProvider getInstance(IcategoriaProductoService iService){
        icategoriaProductoService = iService;
        empresa  = CurrentUser.get() != null ? CurrentUser.get().getEmpresa() : null;
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
        categoria.setEmpresa(empresa);
        categoria = icategoriaProductoService.saveCategoria(categoria);
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
        if(icategoriaProductoService.deleteCategoria(categoria.getId())){
            refreshAll();
            return true;
        }
        return false;
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for categoria: nombre y descripcion
     * 
     * @param filterText
     *            the text to filter by, never null
     */
    public List<CategoriaProducto> setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filtro No puede estar vacio.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            categoriaDataProvider.refreshAll();
            return null;
        }
        this.filterText = filterText.trim();
        return icategoriaProductoService.findByNombreOrDescripcion(filterText, empresa);
    }

    @Override
    public String getId(CategoriaProducto categoria) {
        Objects.requireNonNull(categoria,
                "No se puede obtener un Id para una Categoria Null.");

        return categoria.getId();
    }

    public Collection<CategoriaProducto> findAll() {
        return icategoriaProductoService.findAll(empresa);
    }
}
