package com.losatuendos.alquilerapp.web.dto;

import lombok.Data;

@Data
public class LavanderiaRequest {
    private String ref;
    private boolean prioridad;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isPrioridad() {
        return prioridad;
    }

    public void setPrioridad(boolean prioridad) {
        this.prioridad = prioridad;
    }
}
