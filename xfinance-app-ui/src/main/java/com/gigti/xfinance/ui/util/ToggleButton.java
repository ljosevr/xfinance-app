package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@JsModule("@polymer/paper-toggle-button/paper-toggle-button.js")
@Tag("paper-toggle-button")
public class ToggleButton extends AbstractSinglePropertyField<ToggleButton, Boolean> {
    public ToggleButton() {
        super("checked", false, false);
    }
}
