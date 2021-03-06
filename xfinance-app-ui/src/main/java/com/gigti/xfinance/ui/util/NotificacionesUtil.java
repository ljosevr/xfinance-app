package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

public class NotificacionesUtil {

    private static Dialog dialogProgress;
    private static Button confirmButton;
    private static Button cancelButton;
    private static Dialog dialogConfirmation;

    public static void openProgressBar(String mensaje, boolean cerrarEsc, boolean cerrarClickOutSide){
        ProgressBar progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
        dialogProgress = new Dialog();
        dialogProgress.add(new Label(mensaje));
        dialogProgress.add(progressBar);
        dialogProgress.setCloseOnEsc(cerrarEsc);
        dialogProgress.setCloseOnOutsideClick(cerrarClickOutSide);
        dialogProgress.open();
    }

    public static void closeProgressBar(){
        if(dialogProgress.isOpened())  dialogProgress.open();
    }

    public static void openConfirmationDialog(String mensaje, boolean cerrarEsc, boolean cerrarClickOutSide){

        confirmButton = new Button("SI");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.focus();

        cancelButton = new Button("NO");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        cancelButton.addClickShortcut(Key.ESCAPE);
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Span(mensaje));
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.add(confirmButton, cancelButton);
        layout.add(hlayout);

        dialogConfirmation = new Dialog();
        dialogConfirmation.add(layout);
        dialogConfirmation.setCloseOnEsc(cerrarEsc);
        dialogConfirmation.setCloseOnOutsideClick(cerrarClickOutSide);
        dialogConfirmation.open();
    }

    public static Button getSiButton() {
        return confirmButton;
    }

    public static Button getNoButton() {
        return cancelButton;
    }

    public static Dialog getDialog() {
        if (dialogConfirmation.isOpened()){
            return dialogConfirmation;
        }else {
            Notification.show("Debes Abrir primero una Notificación, Error de Sistema");
            return null;
        }
    }

    public static void showSuccess(String msg){
        Notification notification = Notification.show(msg, 4000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static void showError(String msg){
        Notification notification = Notification.show(msg, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static void showWarn(String msg){
        Notification notification = Notification.show(msg, 4000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    }
}
