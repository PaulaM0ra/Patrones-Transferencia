package com.losatuendos.alquilerapp.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("VESTIDO")
public class VestidoDama extends Prenda {
    private boolean pedreria;
    private String largo;
    private int numeroPiezas;

    public boolean isPedreria() {
        return pedreria;
    }

    public void setPedreria(boolean pedreria) {
        this.pedreria = pedreria;
    }

    public String getLargo() {
        return largo;
    }

    public void setLargo(String largo) {
        this.largo = largo;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(int numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }
}