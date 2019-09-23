package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TopBarComponent extends HorizontalLayout {

    private TextField filter;
    private Button btn;

    public TopBarComponent(TextField filter, Button btn) {
        super();
        this.filter = filter;
        this.btn = btn;
        setWidth("100%");
        add(filter);
        add(btn);
        setVerticalComponentAlignment(Alignment.START, filter);
        expand(filter);
    }
}
