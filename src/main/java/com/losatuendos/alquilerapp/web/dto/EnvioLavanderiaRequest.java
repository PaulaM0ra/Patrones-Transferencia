package com.losatuendos.alquilerapp.web.dto;

import lombok.Data;

@Data
public class EnvioLavanderiaRequest {
    private int cantidad;
    private String tipoProceso;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }
}
