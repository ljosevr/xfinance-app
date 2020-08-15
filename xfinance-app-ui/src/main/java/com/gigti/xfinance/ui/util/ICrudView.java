package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;

public interface ICrudView<T> {

    static final String OPTION_ADD = "ADD";
    static final String OPTION_EDIT = "EDIT";
    static final String OPTION_DELETE = "DELETE";

    default void detailLayout(VerticalLayout layout) {
        layout.addClassName("view");
        layout.setSizeFull();
        layout.setSpacing(false);
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    void closeEditor();
    default void updateList(Grid<T> grid, DataProvider<T, Void> dataProvider) {
        grid.setDataProvider(dataProvider);
    }
    void configureProvider();
    default void configureGrid(Grid<T> grid) {
        grid.setSizeFull();
        grid.addItemDoubleClickListener(evt -> editItem(evt.getItem()));
        grid.setPageSize(50);
    }
    void configureSearchLayout();
    void configureForm();
    void save(ComponentEvent event);
    void delete(ComponentEvent event);

    default void addItem(Grid<T> grid, T object){
        grid.asSingleSelect().clear();
        editItem(object);
    }
    void editItem(Object obj);
    void deleteItem(Object obj);

    default void showForm(boolean show, Dialog form, VerticalLayout layout, TextField filter) {
        if(show){
            form.setVisible(true);
            layout.addClassName("editing");
            form.open();
        }else{
            layout.removeClassName("editing");
            form.close();
            filter.focus();
        }
    }


}
