package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.customfield.CustomField;

public class MyToggleButton extends CustomField<Boolean> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ToggleButton toggleButton = new ToggleButton();

    public MyToggleButton(){
        add(toggleButton);
    }

    @Override
    protected Boolean generateModelValue() {
        return toggleButton.getValue();
    }

    @Override
    protected void setPresentationValue(Boolean aBoolean) {
        if(aBoolean==null){
            toggleButton.setValue(false);
        } else {
            toggleButton.setValue(aBoolean);
        }

    }
}
