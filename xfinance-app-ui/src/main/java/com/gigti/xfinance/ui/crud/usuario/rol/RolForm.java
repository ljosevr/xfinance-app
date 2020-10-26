package com.gigti.xfinance.ui.crud.usuario.rol;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.gigti.xfinance.backend.data.CategoriaProducto;
import com.gigti.xfinance.backend.data.Rol;
import com.gigti.xfinance.backend.data.Vista;
import com.gigti.xfinance.ui.crud.categoria.CategoriaForm;
import com.gigti.xfinance.ui.util.ICrudView;
import com.gigti.xfinance.ui.util.MyResponsiveStep;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftSubMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.tatu.TwinColSelect;

import java.util.*;
import java.util.stream.Collectors;

public class RolForm extends Dialog {
    private final TextField tfRolName;
    private final TextField tfRoldescription;
    private final Checkbox chkRolActivo;
    private final Button btnDelete;
    private final H2 titleForm;
    private Button btnSave;
    private final Binder<Rol> binder;
    private TwinColSelect<Vista> twinColGrid;
    private List<Vista> vistaList;
    private Set<Vista> selectedVistas;

    public RolForm() {
        this.setDraggable(true);
        this.setModal(true);
        this.setResizable(true);
        FormLayout content = new FormLayout();
        content.setClassName("formLayout");
        content.setResponsiveSteps(MyResponsiveStep.getMyList());
        vistaList = new ArrayList<>();
        selectedVistas = new HashSet<>();

        titleForm = new H2("");
        titleForm.addClassName("titleView");

        tfRolName = new TextField("Nombre Rol");
        tfRolName.setRequired(true);
        tfRolName.setValueChangeMode(ValueChangeMode.EAGER);
        tfRolName.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfRolName.setClearButtonVisible(true);
        tfRolName.focus();

        tfRoldescription = new TextField("Descripción Rol");
        tfRoldescription.setRequired(false);
        tfRoldescription.setValueChangeMode(ValueChangeMode.EAGER);
        tfRoldescription.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.MATERIAL_ALWAYS_FLOAT_LABEL);
        tfRoldescription.setClearButtonVisible(true);

        chkRolActivo = new Checkbox("Activo");
        chkRolActivo.setValue(true);

        binder = new BeanValidationBinder<>(Rol.class);
        binder.forField(tfRolName).asRequired("Digite Nombre del Rol").bind(Rol::getNombre,
                Rol::setNombre);
        binder.forField(tfRoldescription).bind(Rol::getDescripcion,
                Rol::setDescripcion);
        binder.forField(chkRolActivo).bind(Rol::isActivo,
                Rol::setActivo);
        binder.bindInstanceFields(this);

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        Button btnClose = new Button("Cerrar");
        btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        btnClose.addClickListener(event -> fireEvent(new RolForm.CloseEvent(this)));
        btnClose.addClickShortcut(Key.ESCAPE);

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SMALL);
        btnDelete.addClickListener(event -> fireEvent(new RolForm.DeleteEvent(this, binder.getBean())));

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnDelete, btnClose);

        VerticalLayout layoutTwinCol = buildGridVistas();
        layoutTwinCol.add(actionsLayout);
        //content.add(titleForm,tfRolName,tfRoldescription,chkRolActivo, layoutTwinCol, actionsLayout);
        content.add(titleForm,tfRolName,tfRoldescription,chkRolActivo, layoutTwinCol);
        content.setColspan(titleForm, content.getResponsiveSteps().size()+1);
        //content.setColspan(actionsLayout, content.getResponsiveSteps().size()+1);
        content.setColspan(layoutTwinCol, content.getResponsiveSteps().size()+1);

        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(false);
        this.add(content);
    }

    private VerticalLayout buildGridVistas() {

        VerticalLayout layout = new VerticalLayout();
        H4 subViewItems = new H4("Seleccionar Vistas");
        subViewItems.addClassName("subTitleView");

        layout.add(subViewItems);

        // Create a new TwinColSelect
        twinColGrid = new TwinColSelect<>();

        // Clear checkbox ticks, does not affect the value
        twinColGrid.clearTicks(TwinColSelect.ColType.BOTH);

        // Set filtering to data provider
        TextField filterField = new TextField("Filter");
        filterField.addValueChangeListener(event -> {
//            dp.setFilter(item -> item.toUpperCase().startsWith(event.getValue().toUpperCase()));
        });
        // Reset value when filter is changed
        twinColGrid.setFilterMode(TwinColSelect.FilterMode.RESETVALUE);
        twinColGrid.setItems(vistaList);
        twinColGrid.updateSelection(selectedVistas,selectedVistas);
        layout.add(twinColGrid);

        return layout;

    }

    public void setRol(Rol rol, String title, String type, List<Vista> allVistas) {
        binder.setBean(rol);
        Set<Vista> setAllVista = new HashSet<>();
        setAllVista.addAll(formatVistaName(allVistas));
        twinColGrid.setItems(setAllVista);
        System.out.println("AllVistas: "+setAllVista);

        if(rol != null && StringUtils.isNotBlank(rol.getId())) {

            Set<Vista> rolVistas = new HashSet<>();
            for(Vista v : setAllVista) {
                for(Vista viewRol : rol.getVistas()) {
                    if(viewRol.getRouteVista() != null
                    && v.getRouteVista() != null
                    && viewRol.getRouteVista().equalsIgnoreCase(v.getRouteVista())) {
                        rolVistas.add(v);
                        break;
                    }
                }
            }
            twinColGrid.setValue(rolVistas);
        }

        setReadOnlyByDelete(type.equals(ICrudView.OPTION_DELETE));
        btnDelete.setEnabled(!ICrudView.OPTION_ADD.equals(type));
        titleForm.setText(title);
        tfRolName.focus();
    }

    private Set<Vista> formatVistaName(List<Vista> allVistas) {

        Set<Vista> resultList = new HashSet<>();
        for(Vista view : allVistas){
            if (view.getVistaPadre() == null) {
                if (view.getSubVistas().size() == 0) {
                    view.setRouteName(view.getNombreVista());
                    addToResultList(view, resultList);

                }
                if (view.getSubVistas().size() > 0) {
                    List<Vista> listTemp = allVistas.stream()
                            .filter(v -> {
                                if(v.getVistaPadre() != null) {
                                    return v.getVistaPadre().getId().equals(view.getId());
                                }
                                return false;
                            })
                            .collect(Collectors.toList());

                    for (Vista v : listTemp) {
                        v.setRouteName(view.getNombreVista() +" - "+v.getNombreVista());
                        addToResultList(v, resultList);
                    }
                }
            }
        }
        return resultList;
    }

    private void addToResultList(Vista view, Set<Vista> resultList) {
        if(StringUtils.isNotBlank(view.getRouteName()) && !view.isViewRoot()) {
            resultList.add(view);
        }
    }

    private void setReadOnlyByDelete(boolean readOnly) {
        btnSave.setVisible(!readOnly);
        btnDelete.setText(readOnly ? "Sí, Eliminar" : "Eliminar");
        tfRolName.setReadOnly(readOnly);
        tfRoldescription.setReadOnly(readOnly);
        chkRolActivo.setReadOnly(readOnly);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()){
            if(twinColGrid.getSelectedItems().size() > 0) {
                Rol rol = binder.getBean();
                rol.setVistas(twinColGrid.getSelectedItems());
                fireEvent(new RolForm.SaveEvent(this, rol));
            } else {
                NotificacionesUtil.showError("Para Guardar Debes agregar Minimo una(1) vista al Rol");
            }
        }
    }

    // Events
    public static abstract class RolFormEvent extends ComponentEvent<RolForm> {
        private Rol rol;

        RolFormEvent(RolForm source, Rol rol) {
            super(source, false);
            this.rol = rol;
        }
        public Rol getRol() {
            return rol;
        }
    }

    public static class SaveEvent extends RolForm.RolFormEvent {
        SaveEvent(RolForm source, Rol rol) {
            super(source, rol);
        }
    }

    public static class DeleteEvent extends RolForm.RolFormEvent {
        DeleteEvent(RolForm source, Rol rol) {
            super(source, rol);
        }
    }

    public static class CloseEvent extends RolForm.RolFormEvent {
        CloseEvent(RolForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
