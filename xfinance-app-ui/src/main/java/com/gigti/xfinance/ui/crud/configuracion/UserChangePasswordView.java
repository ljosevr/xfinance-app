package com.gigti.xfinance.ui.crud.configuracion;

import com.gigti.xfinance.backend.data.Usuario;
import com.gigti.xfinance.backend.data.dto.UsuarioChangePasswordDTO;
import com.gigti.xfinance.backend.data.enums.TipoEmpresaEnum;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.Response;
import com.gigti.xfinance.backend.services.UsuarioService;
import com.gigti.xfinance.ui.MainLayout;
import com.gigti.xfinance.ui.authentication.CurrentUser;
import com.gigti.xfinance.ui.util.NotificacionesUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = Constantes.VIEW_R_CONFIG_PASSWORD, layout = MainLayout.class)
@PageTitle(value = Constantes.VIEW_CONFIG_PASSWORD +" | "+ Constantes.VIEW_MAIN)
public class UserChangePasswordView extends VerticalLayout {

    private PasswordField pfNewPassword2;
    private PasswordField pfNewPassword1;
    private PasswordField pfOldPassword;
    private UsuarioService usuarioService;
    private Binder<UsuarioChangePasswordDTO> binder;

    public UserChangePasswordView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        //setSizeFull();
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        this.setSizeUndefined();
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setBoxSizing(BoxSizing.CONTENT_BOX);

        H1 title = new H1(Constantes.VIEW_CONFIG_PASSWORD.toUpperCase());
        title.addClassName("titleView2");

        FormLayout formLayout = new FormLayout();
        formLayout.setClassName("formLayout");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2),
                new FormLayout.ResponsiveStep("40em", 3));

        pfOldPassword = new PasswordField("Password Actual");
        pfOldPassword.setRequired(true);
        pfOldPassword.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        pfOldPassword.setClearButtonVisible(true);

        pfNewPassword1 = new PasswordField("Password Nuevo");
        pfNewPassword1.setRequired(true);
        pfNewPassword1.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        pfNewPassword1.setClearButtonVisible(true);

        pfNewPassword2 = new PasswordField("Confirmar Password Nuevo");
        pfNewPassword2.setRequired(true);
        pfNewPassword2.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        pfNewPassword2.setClearButtonVisible(true);

        formLayout.add(pfOldPassword,pfNewPassword1,pfNewPassword2);

        binder = new BeanValidationBinder<>(UsuarioChangePasswordDTO.class);
        binder.forField(pfOldPassword).asRequired("Digite el Password Actual")
                .bind(UsuarioChangePasswordDTO::getOldPassword, UsuarioChangePasswordDTO::setOldPassword);

        binder.forField(pfNewPassword1).asRequired("Digite el Nuevo Password")
                .withValidator(newPass -> newPass.length() >= 4,"Nueva Contraseña debe contener al menos 4 caracteres")
                .withValidator(pass1 -> pfNewPassword2.isEmpty() || pass1.equals(pfNewPassword2.getValue()),
                            "La Nueva Contraseña NO coincide, Verifica")
                .bind(UsuarioChangePasswordDTO::getNewPassword1, UsuarioChangePasswordDTO::setNewPassword1);

        binder.forField(pfNewPassword2).asRequired("Confirme el Nuevo Password")
                .withValidator(newPass -> newPass.length() >= 4,"Nueva Contraseña debe contener al menos 4 caracteres")
                .withValidator(pass2 -> pfNewPassword1.isEmpty() || pass2.equals(pfNewPassword1.getValue()),
                            "La Nueva Contraseña NO coincide, Verifica")
                .bind(UsuarioChangePasswordDTO::getNewPassword2, UsuarioChangePasswordDTO::setNewPassword2);

        Button btnSave = new Button("Guardar");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(event -> validateAndSave());
        btnSave.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(event -> btnSave.setEnabled(binder.isValid()));

        Button btnClean = new Button("Limpiar");
        btnClean.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnClean.addClickListener(event -> cleanForm());
        btnClean.addClickShortcut(Key.ESCAPE);

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.add(btnSave, btnClean);

        add(title, formLayout, actionsLayout);

    }

    private void validateAndSave() {

        BinderValidationStatus status = binder.validate();
        if(status.hasErrors()) {
            status.notifyBindingValidationStatusHandlers();
        }

        if(CurrentUser.get().getEmpresa().getTipoEmpresa().equals(TipoEmpresaEnum.DEMO)) {
            NotificacionesUtil.showError("Usuario DEMO - NO Puede cambiar Password");
        } else {
            if (binder.isValid()) {
                if (pfNewPassword1.getValue().equals(pfNewPassword2.getValue())) {
                    Response response = usuarioService.changePassword(CurrentUser.get().getId(), pfOldPassword.getValue(), pfNewPassword1.getValue(), pfNewPassword2.getValue());
                    if (response.isSuccess()) {
                        NotificacionesUtil.showSuccess(response.getMessage());
                        CurrentUser.set((Usuario) response.getObject());
                        UI.getCurrent().navigate(MainLayout.class);
                    } else {
                        NotificacionesUtil.showError(response.getMessage());
                    }
                }
            }
        }
    }

    private void cleanForm() {
        pfOldPassword.clear();
        pfNewPassword1.clear();
        pfNewPassword2.clear();
    }
}
