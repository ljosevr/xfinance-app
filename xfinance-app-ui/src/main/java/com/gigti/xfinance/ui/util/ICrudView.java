package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.ComponentEvent;

public interface ICrudView {
    void closeEditor();
    void updateList();
    void configureProvider();
    void configureGrid();
    void configureSearchLayout();
    void configureForm();
    void edit(Object obj);
    void save(ComponentEvent event);
    void delete(ComponentEvent event);
    void addItem();
}
