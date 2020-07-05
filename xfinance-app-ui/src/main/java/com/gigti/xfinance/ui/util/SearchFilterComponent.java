package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class SearchFilterComponent extends VerticalLayout {

    private TextField filter;
    private Button btnSearch;
    private Button btnAdd;

    public SearchFilterComponent(String labelAddBtn, String labelFilter, String placeHolderFilter, boolean visibleSearchBtn, boolean visibleAddBtn) {
        this.addClassName("searchBar");

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);
        filter.getElement().setAttribute("title", placeHolderFilter);

        btnSearch = new Button("", new Icon(VaadinIcon.SEARCH));
        btnSearch.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnSearch.getElement().setAttribute("title", "Buscar - F4");
        btnSearch.setVisible(visibleSearchBtn);
        btnSearch.addClickShortcut(Key.F4);

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.getElement().setAttribute("title", "Agregar - F7");
        btnAdd.addClickShortcut(Key.F7);
        btnAdd.setVisible(visibleAddBtn);

        HorizontalLayout hLayout = new HorizontalLayout(filter,btnSearch,btnAdd);
        hLayout.setSizeFull();
        hLayout.expand(filter);
        add(hLayout);
    }

    public TextField getFilter() {
        return filter;
    }

    public Button getBtnSearch() {
        return btnSearch;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(Button btnAdd) {
        this.btnAdd = btnAdd;
    }

}
