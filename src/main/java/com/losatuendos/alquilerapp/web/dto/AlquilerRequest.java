package com.losatuendos.alquilerapp.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AlquilerRequest {
    private String clienteId;
    private String empleadoId;
    private List<PrendaRequest> prendas;
    private LocalDate fechaAlqui;

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(String empleadoId) {
        this.empleadoId = empleadoId;
    }

    public List<PrendaRequest> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<PrendaRequest> prendas) {
        this.prendas = prendas;
    }

    public LocalDate getFechaAlqui() {
        return fechaAlqui;
    }

    public void setFechaAlqui(LocalDate fechaAlqui) {
        this.fechaAlqui = fechaAlqui;
    }
}