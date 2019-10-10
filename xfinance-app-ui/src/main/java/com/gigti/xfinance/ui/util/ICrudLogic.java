package com.gigti.xfinance.ui.util;

import com.gigti.xfinance.backend.data.Empresa;

import java.util.List;

public interface ICrudLogic<T> {
    void init();
    boolean acceder();
    void cancelar();
    private void setFragmentParameter(String id){};
    void enter(String id);
    T find(String id);
    void guardar(T object);
    void eliminar(T object);
    void editar(T object);
    void nuevo();
    void rowSelected(T object);
    List<T> findAll();
    List<T> setFilter(String filterText);

}
