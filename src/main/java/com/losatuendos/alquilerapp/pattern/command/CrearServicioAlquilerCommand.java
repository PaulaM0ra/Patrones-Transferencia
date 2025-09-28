package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import java.time.LocalDate;
import java.util.List;

public class CrearServicioAlquilerCommand implements OperacionCommand {
    private final String clienteId;
    private final String empleadoId;
    private final List<PrendaDTO> dtos;
    private final LocalDate fechaAlqui;
    private final NegocioAlquilerFacade facade;

    public CrearServicioAlquilerCommand(String clienteId, String empleadoId, List<PrendaDTO> dtos, LocalDate fechaAlqui, NegocioAlquilerFacade facade) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.dtos = dtos;
        this.fechaAlqui = fechaAlqui;
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.crearServicioAlquiler(clienteId, empleadoId, dtos, fechaAlqui);
    }
}
