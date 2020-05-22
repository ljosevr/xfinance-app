/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.ui.authentication;

import com.gigti.xfinance.backend.data.Usuario;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

/**
 * Class for retrieving and setting the name of the current user of the current
 * session (without using JAAS). All methods of this class require that a
 * {@link VaadinRequest} is bound to the current thread.
 * 
 * 
 * @see VaadinService#getCurrentRequest()
 */
public final class CurrentUser {

    private static Usuario current;
    /**
     * The attribute key used to store the username in the session.
     */
    public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = LoginView.class
            .getCanonicalName();

    public static final String CURRENT_USER_IP_ADDRESS_SESSION_ATTRIBUTE_KEY = "IP_ADDRESS";

    private CurrentUser() {
    }

    /**
     * Returns the name of the current user stored in the current session, or an
     * empty string if no user name is stored.
     * 
     * @throws IllegalStateException
     *             if the current session cannot be accessed.
     */
    public static Usuario get() {
        Usuario currentUser = (Usuario) getCurrentRequest().getWrappedSession()
                .getAttribute(CURRENT_USER_SESSION_ATTRIBUTE_KEY);

        String ip_address_session = (String) getCurrentRequest().getWrappedSession()
                .getAttribute(CURRENT_USER_IP_ADDRESS_SESSION_ATTRIBUTE_KEY);

        //NotificacionesUtil.showWarn(getClientIpAddr(VaadinRequest.getCurrent()) + " - "+VaadinRequest.getCurrent().getHeader("X-Forwarded-For"));

        if(ip_address_session != null && !ip_address_session.isEmpty()) {
            String ip_actual = VaadinSession.getCurrent().getBrowser().getAddress();
            if(ip_actual.equals(ip_address_session)) {
                if (currentUser == null) {
                    return current;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        if (currentUser == null) {
            return current;
        }

        return currentUser;
    }

    /**
     * Sets the name of the current user and stores it in the current session.
     * Using a {@code null} username will remove the username from the session.
     * 
     * @throws IllegalStateException
     *             if the current session cannot be accessed.
     */
    public static void set(Usuario currentUser) {
        if (currentUser == null) {
            getCurrentRequest().getWrappedSession().removeAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY);

            getCurrentRequest().getWrappedSession().removeAttribute(
                    CURRENT_USER_IP_ADDRESS_SESSION_ATTRIBUTE_KEY);

            current = null;
        } else {
            getCurrentRequest().getWrappedSession().setAttribute(
                    CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser);

            getCurrentRequest().getWrappedSession().setAttribute(
                    CURRENT_USER_IP_ADDRESS_SESSION_ATTRIBUTE_KEY, VaadinSession.getCurrent().getBrowser().getAddress());

            current = currentUser;
        }
    }

    private static VaadinRequest getCurrentRequest() {
        VaadinRequest request = VaadinService.getCurrentRequest();
        if (request == null) {
            throw new IllegalStateException(
                    "No request bound to current thread.");
        }
        return request;
    }

    private static String getClientIpAddr(VaadinRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
