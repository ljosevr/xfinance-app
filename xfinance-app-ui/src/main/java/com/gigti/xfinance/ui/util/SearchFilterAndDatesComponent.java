package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class SearchFilterAndDatesComponent extends FormLayout {

    private final TextField filter;
    private final DatePicker dateStart;
    private final DatePicker dateEnd;

    private final Button btnSearch;
    private final Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;

    public SearchFilterAndDatesComponent(String labelAddBtn, String labelFilter, String placeHolderFilter, boolean visibleSearchBtn, boolean visibleAddBtn, String labelDateStart, String labelDateEnd) {
        this.addClassName("searchBar");

        this.setResponsiveSteps(MyResponsiveStep.getMyList());

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);

        btnSearch = new Button("", new Icon(VaadinIcon.SEARCH));
        btnSearch.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnSearch.setVisible(visibleSearchBtn);
        btnSearch.addClickShortcut(Key.F4);

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.addClickShortcut(Key.F7);
        btnAdd.setVisible(visibleAddBtn);

        dateStart = new DatePicker();
        dateStart.setPlaceholder(labelDateStart);
        dateStart.getElement().setAttribute("theme", "align-center");
        dateStart.getElement().setAttribute("theme", "small");

        dateEnd = new DatePicker();
        dateEnd.setPlaceholder(labelDateEnd);
        dateEnd.getElement().setAttribute("theme", "align-center");
        dateEnd.getElement().setAttribute("theme", "small");

        add(filter,dateStart, dateEnd, btnSearch, btnAdd);
    }

    public SearchFilterAndDatesComponent(String labelAddBtn, boolean visibleAddBtn, String labelFilter, String placeHolderFilter, String labelSearchBtn, boolean visibleSearchBtn,
                                         String labelBtnEdit, boolean visibleBtnEdit,String labelBtnDelete, boolean visibleBtnDelete, String labelDateStart, String labelDateEnd) {
        this.addClassName("searchBar");
        this.setResponsiveSteps(MyResponsiveStep.getMyList());

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.getElement().setAttribute("title", placeHolderFilter);
        filter.setMinWidth("300px");

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.getElement().setAttribute("title", "Agregar - F4");
        btnAdd.addClickShortcut(Key.F4);
        btnAdd.setVisible(visibleAddBtn);
        btnAdd.setMaxWidth("35px");

        btnEdit = new Button(labelBtnEdit, new Icon(VaadinIcon.EDIT));
        btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
        btnEdit.getElement().setAttribute("title", "Editar - F7");
        btnEdit.addClickShortcut(Key.F7);
        btnEdit.setVisible(visibleBtnEdit);
        btnEdit.setMaxWidth("35px");

        btnDelete = new Button(labelBtnDelete, new Icon(VaadinIcon.TRASH));
        btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
        btnDelete.getElement().setAttribute("title", "Eliminar - F8");
        btnDelete.addClickShortcut(Key.F8);
        btnDelete.setVisible(visibleBtnDelete);
        btnDelete.setMaxWidth("35px");

        btnSearch = new Button(labelSearchBtn, new Icon(VaadinIcon.SEARCH));
        btnSearch.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnSearch.getElement().setAttribute("title", "Buscar - F9");
        btnSearch.setVisible(visibleSearchBtn);
        btnSearch.addClickShortcut(Key.F9);
        btnSearch.setMaxWidth("35px");

        dateStart = new DatePicker();
        dateStart.setPlaceholder(labelDateStart);
        dateStart.getElement().setAttribute("theme", "align-center");
        dateStart.getElement().setAttribute("theme", "small");
        dateStart.setMaxWidth("190px");

        dateEnd = new DatePicker();
        dateEnd.setPlaceholder(labelDateEnd);
        dateEnd.getElement().setAttribute("theme", "align-center");
        dateEnd.getElement().setAttribute("theme", "small");
        dateEnd.setMaxWidth("190px");

        add(btnAdd, btnEdit, btnDelete, filter,dateStart, dateEnd, btnSearch);
    }

    public TextField getFilter() {
        return filter;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnSearch() {
        return btnSearch;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public DatePicker getDateStart() {
        return dateStart;
    }

    public DatePicker getDateEnd() {
        return dateEnd;
    }
}
