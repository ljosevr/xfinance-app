/*
 * Copyright (c) 2019. Propiedad Exclusiva de GigTi.
 * Derechos reservados.
 * Toda copia o utilización de este codigo debe estar sustentado por escrito por GigTi, si no será considerado plagio y pirateria. Por consiguiente será llevado ante la justicia correspondiente.
 */

package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "empresas")
public class Empresa extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_ide_id")
    private TipoIde tipoIde;

    @NotNull
    @NotEmpty
    @Size(min= 4)
    @Column(unique = true)
    private String identificacion;

    @Size(min= 3, max = 6)
    @Column(name = "codigo_empresa", unique = true, nullable = false)
    private String codigoEmpresa;

    @NotNull
    @NotEmpty()
    @Size(min= 4)
    @Column(name = "nombre_empresa", unique = true)
    private String nombreEmpresa;

    private String direccion;

    private String telefono;

    @NotNull
    private boolean activo;

    @NotNull
    private boolean eliminado;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fechaActivacion;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fechaDesactivacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_empresa_id")
    private TipoEmpresa tipoEmpresa;

    @Transient
    private String activoS;

    @OneToMany(mappedBy = "empresa",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos;

    @OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoriaProducto> categoriaProductos;

    public Empresa(TipoIde tipoIde, String identificacion, String nombreEmpresa, String direccion, Date fechaActivacion, Date fechaDesactivacion, TipoEmpresa tipo) {
        this.tipoIde = tipoIde;
        this.identificacion = identificacion;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.activo = true;
        this.fechaActivacion = fechaActivacion;
        this.fechaDesactivacion = fechaDesactivacion;
        this.eliminado = false;
        this.tipoEmpresa = tipo;
    }

    public Empresa(TipoIde tipoIde, String identificacion, String nombreEmpresa, String direccion, Boolean activo, Date fechaActivacion, Date fechaDesactivacion, TipoEmpresa tipo, String codigoEmpresa) {
        this.tipoIde = tipoIde;
        this.identificacion = identificacion;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.activo = activo;
        this.fechaActivacion = fechaActivacion;
        this.fechaDesactivacion = fechaDesactivacion;
        this.eliminado = false;
        this.tipoEmpresa = tipo;
        this.codigoEmpresa = codigoEmpresa;
    }

    @Override
    public String toString() {
        return nombreEmpresa;
    }

}
