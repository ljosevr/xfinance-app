package com.gigti.xfinance.ui.util;

import com.vaadin.flow.component.page.Page;

public class ValidateDevice {

    private static boolean result;

    public static boolean isMobile(Page page) {
        page.retrieveExtendedClientDetails(details -> {
            if(details.getWindowInnerWidth() < 600) {
                result = true;
            } else {
                result = false;
            }
        });
        return result;
    }
}
