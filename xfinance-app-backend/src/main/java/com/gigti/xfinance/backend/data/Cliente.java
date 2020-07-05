package com.gigti.xfinance.backend.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "clientes")
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends AbstractEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Persona persona;

    @NotNull(message = "No puede estar Vacio")
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    private boolean eliminado;

}
