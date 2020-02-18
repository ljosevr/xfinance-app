package com.gigti.xfinance.ui.util;

import com.gigti.xfinance.backend.data.dto.PventaDTO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TopBarComponent extends HorizontalLayout {

    private Component layout;
    private TextField filter;
    private Button btn;
    private ComboBox<PventaDTO> filter1;

    public TopBarComponent(TextField filter, Button btn) {
        super();
        this.addClassName("toolbar");
        this.filter = filter;
        this.btn = btn;
        setWidth("100%");
        add(filter);
        add(btn);
        setVerticalComponentAlignment(Alignment.START, filter);
        expand(filter);
    }

    public TopBarComponent(ComboBox<PventaDTO> filter) {
        super();
        this.filter1 = filter;
        setWidth("100%");
        add(filter);
        setVerticalComponentAlignment(Alignment.START, filter);
        expand(filter);
    }

    public TopBarComponent() {
        setWidth("100%");
        setVerticalComponentAlignment(Alignment.START);
    }
}
