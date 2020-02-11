/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "vistas")
public class Vista extends AbstractEntity {

    @Column(unique = true)
    private String nombreVista;
    @Column(unique = true)

    private String routeVista;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Vista vistaPadre;

    private int orderVista;

    private String iconMenu;

    @OneToMany(mappedBy="vistaPadre",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Vista> subVistas;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy ="vistas")
    private Set<Rol> roles;

    public Vista() {
    }

    public Vista(String nombreVista, String routeVista, Vista vistaPadre, int orderVista, String iconMenu) {
        this.nombreVista = nombreVista;
        this.routeVista = routeVista;
        this.vistaPadre = vistaPadre;
        this.orderVista = orderVista;
        this.iconMenu = iconMenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vista vista = (Vista) o;
        return orderVista == vista.orderVista &&
                Objects.equals(nombreVista, vista.nombreVista) &&
                Objects.equals(routeVista, vista.routeVista) &&
                Objects.equals(vistaPadre, vista.vistaPadre) &&
                Objects.equals(iconMenu, vista.iconMenu) &&
                Objects.equals(subVistas, vista.subVistas) &&
                Objects.equals(roles, vista.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nombreVista, routeVista, vistaPadre, orderVista, iconMenu, subVistas, roles);
    }

    @Override
    public String toString() {
        return nombreVista;
    }
}
