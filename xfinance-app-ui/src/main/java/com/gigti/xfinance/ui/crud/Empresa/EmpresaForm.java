package com.gigti.xfinance.ui.crud.Empresa;

import com.gigti.xfinance.backend.data.Empresa;
import com.gigti.xfinance.backend.data.TipoIde;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

public class EmpresaForm extends FormLayout {

    private Button btnSave;
    private Button btnDiscard;
    private Button btnDelete;

    private EmpresaCrudLogic viewLogic;
    private Binder<Empresa> binder;
    private Empresa currentEmpresa;

    public EmpresaForm(EmpresaCrudLogic empresaCrudLogic) {

        this.setResponsiveSteps(
                new ResponsiveStep("25em", 1),
                new ResponsiveStep("32em", 2),
                new ResponsiveStep("40em", 3));

        H4 title = new H4("Crear o Editar Empresa");
        this.add(title,3);

        viewLogic = empresaCrudLogic;

        TextField tfNombreEmpresa = new TextField("Nombre Empresa");
        tfNombreEmpresa.setRequired(true);

        ComboBox<TipoIde> cbTipoIde = new ComboBox<>();
        cbTipoIde.setLabel("Tipo Ide");
        cbTipoIde.setItems(TipoIde.getListTipos());
        cbTipoIde.setRequired(true);

        TextField tfIdentificacion = new TextField("Identificación");
        tfIdentificacion.setRequired(true);

        TextField tfDireccion = new TextField("Dirección");
        tfDireccion.setRequired(false);

        TextField tfTelefono = new TextField("Telefono");
        tfTelefono.setRequired(false);

        Checkbox cbActivo = new Checkbox("Activo");
        cbActivo.setValue(true);

        binder = new BeanValidationBinder<>(Empresa.class);
        binder.forField(tfNombreEmpresa).bind(Empresa::getNombreEmpresa, Empresa::setNombreEmpresa);
        binder.forField(cbTipoIde).bind(Empresa::getTipoIde, Empresa::setTipoIde);
        binder.forField(tfIdentificacion).bind(Empresa::getIdentificacion, Empresa::setIdentificacion);
        binder.forField(tfDireccion).bind(Empresa::getDireccion, Empresa::setDireccion);
        binder.forField(tfTelefono).bind(Empresa::getTelefono, Empresa::setTelefono);
        binder.forField(cbActivo).bind(Empresa::isActivo, Empresa::setActivo);
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            boolean isValid = !event.hasValidationErrors();
            boolean hasChanges = binder.hasChanges();
            btnSave.setEnabled(hasChanges && isValid);
            btnDiscard.setEnabled(hasChanges);
        });

        btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> {
            if (currentEmpresa != null && binder.writeBeanIfValid(currentEmpresa)) {
                viewLogic.guardar(currentEmpresa);
            }
        });
        btnSave.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        btnDiscard = new Button("Descartar Cambios");
        btnDiscard.addClickListener(
                event -> viewLogic.editar(currentEmpresa));

        Button btnCancel = new Button("Cancelar");
        btnCancel.addClickListener(event -> viewLogic.cancelar());
        btnCancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelar())
                .setFilter("event.key == 'Escape'");

        btnDelete = new Button("Eliminar");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addClickListener(event -> {
            if (currentEmpresa != null) {
                viewLogic.eliminar(currentEmpresa);
            }
        });

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave,btnDiscard);
        HorizontalLayout actionsLayout2 = new HorizontalLayout();
        actionsLayout.add(btnDelete,btnCancel);
        this.add(tfNombreEmpresa,cbTipoIde,tfIdentificacion,tfDireccion, tfTelefono,cbActivo,actionsLayout,actionsLayout2);
        this.setColspan(actionsLayout, 3);
        this.setColspan(actionsLayout2,2);
    }

    public void edit(Empresa empresa) {
        if (empresa == null) {
            empresa = new Empresa();
            empresa.setActivo(true);
            btnDelete.setEnabled(false);
        } else if(StringUtils.isBlank(empresa.getId())){
            empresa.setActivo(true);
            btnDelete.setEnabled(false);
        } else {
            btnDelete.setEnabled(true);
        }
        currentEmpresa = empresa;
        binder.readBean(empresa);
    }
}
