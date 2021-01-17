package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SearchFilterComponent extends FormLayout {

    private Anchor anchor;
    private FileDownloadWrapper fileWrapper;
    private Button btnToPdf;
    private TextField filter;
    private Button btnSearch;
    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;

    public SearchFilterComponent(String labelAddBtn, String labelFilter, String placeHolderFilter, boolean visibleSearchBtn, boolean visibleAddBtn) {
        this.addClassName("searchBar");
        this.setResponsiveSteps(MyResponsiveStep.getMyList());
        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.addFocusShortcut(Key.F3);
        filter.getElement().setAttribute("title", placeHolderFilter);
        filter.setMinWidth("300px");

        btnSearch = new Button("", new Icon(VaadinIcon.SEARCH));
        btnSearch.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnSearch.getElement().setAttribute("title", "Buscar - F4");
        btnSearch.setVisible(visibleSearchBtn);
        btnSearch.addClickShortcut(Key.F4);

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.getElement().setAttribute("title", "Agregar - F7");
        btnAdd.addClickShortcut(Key.F7);
        btnAdd.setVisible(visibleAddBtn);

        add(filter,btnSearch,btnAdd);
    }

    public SearchFilterComponent(String labelAddBtn, boolean visibleAddBtn, String labelFilter, String placeHolderFilter, String labelSearchBtn, boolean visibleSearchBtn,
                                 String labelBtnEdit, boolean visibleBtnEdit,String labelBtnDelete, boolean visibleBtnDelete) {

        this.addClassName("searchBar");
        this.setResponsiveSteps(MyResponsiveStep.getMyList());

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.getElement().setAttribute("title", placeHolderFilter);
        filter.setMaxWidth("300px");

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.getElement().setAttribute("title", "Agregar - F6");
        btnAdd.addClickShortcut(Key.F6);
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

        this.add(btnAdd, btnEdit, btnDelete, filter, btnSearch);
    }

    public SearchFilterComponent(String labelAddBtn, boolean visibleAddBtn, String labelFilter, String placeHolderFilter, String labelSearchBtn, boolean visibleSearchBtn,
                                 String labelBtnEdit, boolean visibleBtnEdit,String labelBtnDelete, boolean visibleBtnDelete,
                                 boolean visibleToPdf) {

        this.addClassName("searchBar");
        this.setResponsiveSteps(MyResponsiveStep.getMyList());

        filter = new TextField(labelFilter);
        filter.setPlaceholder(placeHolderFilter);
        filter.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.setClearButtonVisible(true);
        filter.setAutoselect(true);
        filter.getElement().setAttribute("title", placeHolderFilter);
        filter.setMaxWidth("300px");

        btnAdd = new Button(labelAddBtn);
        btnAdd.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        btnAdd.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnAdd.getElement().setAttribute("title", "Agregar - F6");
        btnAdd.addClickShortcut(Key.F6);
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

        btnToPdf = new Button("", new Icon(VaadinIcon.DOWNLOAD));
        btnToPdf.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnToPdf.getElement().setAttribute("title", "Generar Reporte - F10");
        btnToPdf.setVisible(visibleToPdf);
        btnToPdf.addClickShortcut(Key.F10);
        btnToPdf.setMaxWidth("35px");

        //fileWrapper = new FileDownloadWrapper(new StreamResource("reporte.pdf", () -> new ByteArrayInputStream("foo".getBytes())));
        //fileWrapper.wrapComponent(btnToPdf);

        this.add(btnAdd, btnEdit, btnDelete, filter, btnSearch, btnToPdf);
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

    public FileDownloadWrapper getWrapper() {
        return fileWrapper;
    }
    public Button getBtnToPdf() {
        return btnToPdf;
    }

    public Anchor getAnchor(){
        return anchor;
    }
}
