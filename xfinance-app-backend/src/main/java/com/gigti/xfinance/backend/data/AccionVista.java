package com.gigti.xfinance.backend.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "acciones_vistas")
public class AccionVista extends AbstractEntity {

}
