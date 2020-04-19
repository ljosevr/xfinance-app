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

    private TextField filter;
    private DatePicker dateStart;
    private DatePicker dateEnd;

    private Button btnSearch;
    private Button btnAdd;

    public SearchFilterAndDatesComponent(String labelAddBtn, String labelFilter, String placeHolderFilter, boolean visibleSearchBtn, boolean visibleAddBtn, String labelDateStart, String labelDateEnd) {
        this.addClassName("searchBar");

        this.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 4));

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

        //HorizontalLayout hLayout = new HorizontalLayout(filter,btnSearch,dateStart, dateEnd, btnAdd);
        //hLayout.setSizeFull();
        //hLayout.expand(filter);
        add(filter,btnSearch,dateStart, dateEnd, btnAdd);
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

    public DatePicker getDateStart() {
        return dateStart;
    }

    public DatePicker getDateEnd() {
        return dateEnd;
    }
}
