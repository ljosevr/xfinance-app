package com.gigti.xfinance.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import dev.mett.vaadin.tooltip.Tooltips;

public class MyButton extends Button {

    public MyButton(String text, String toolTip) {
        super(text);
        Tooltips.getCurrent().setTooltip(this, toolTip);
    }

    public MyButton(Component icon, String toolTip) {
        super(icon);
        Tooltips.getCurrent().setTooltip(this, toolTip);
    }

    public static Button MyButton(String text, Component icon, String toolTip, ButtonVariant typeButton, boolean small, boolean enabled) {
        Button btn = new Button(text, icon);
        btn.addThemeVariants(typeButton);
        if(small) {
            btn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        }

        if(!toolTip.isEmpty()) {
            Tooltips.getCurrent().setTooltip(btn, toolTip);
        }
        btn.setEnabled(enabled);
        return btn;
    }
}
