package com.losatuendos.alquilerapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PrendaDTO {
    private String ref;
    private String color;
    private String marca;
    private String talla;
    private double valorAlquiler;
    // atributos específicos
    private Boolean pedreria;
    private String largo;
    private Integer numeroPiezas;
    private String tipoTraje;
    private String accesorio;
    private String nombreDisfraz;
    // El tipo discriminador debe coincidir con (vestido, traje o disfraz)
    private String tipoDiscriminador;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public double getValorAlquiler() {
        return valorAlquiler;
    }

    public void setValorAlquiler(double valorAlquiler) {
        this.valorAlquiler = valorAlquiler;
    }

    public Boolean getPedreria() {
        return pedreria;
    }

    public void setPedreria(Boolean pedreria) {
        this.pedreria = pedreria;
    }

    public String getLargo() {
        return largo;
    }

    public void setLargo(String largo) {
        this.largo = largo;
    }

    public Integer getNumeroPiezas() {
        return numeroPiezas;
    }

    public void setNumeroPiezas(Integer numeroPiezas) {
        this.numeroPiezas = numeroPiezas;
    }

    public String getTipoTraje() {
        return tipoTraje;
    }

    public void setTipoTraje(String tipoTraje) {
        this.tipoTraje = tipoTraje;
    }

    public String getAccesorio() {
        return accesorio;
    }

    public void setAccesorio(String accesorio) {
        this.accesorio = accesorio;
    }

    public String getNombreDisfraz() {
        return nombreDisfraz;
    }

    public void setNombreDisfraz(String nombreDisfraz) {
        this.nombreDisfraz = nombreDisfraz;
    }

    public String getTipoDiscriminador() {
        return tipoDiscriminador;
    }

    public void setTipoDiscriminador(String tipoDiscriminador) {
        this.tipoDiscriminador = tipoDiscriminador;
    }
}
