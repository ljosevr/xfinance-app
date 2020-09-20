package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@Table(name = "proveedores",
        uniqueConstraints={@UniqueConstraint(columnNames = {"identificacion" , "empresa_id"}),
                           @UniqueConstraint(columnNames = {"nombre_proveedor" , "empresa_id"}),
                           @UniqueConstraint(columnNames = {"email" , "empresa_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor extends AbstractEntity {

    @NotNull(message = "No puede estar Vacio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_ide_id")
    private TipoIde tipoIde;

    @NotNull(message = "No puede estar Vacio")
    @Size(min= 4)
    private String identificacion;

    @NotNull(message = "No puede estar Vacio")
    @Size(min= 4, message = "Longitud Minima de 4")
    @Column(name = "nombre_proveedor")
    private String nombre;

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

    @Transient
    private String activoS;

    @NotNull(message = "No puede estar Vacio")
    @Email
    private String email;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Empresa empresa;

    public String getActivoS() {
        return isActivo() ? "SI" : "NO";
    }
}
