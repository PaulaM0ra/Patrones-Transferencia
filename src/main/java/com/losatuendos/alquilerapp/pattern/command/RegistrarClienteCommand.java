package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.dto.ClienteDTO;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;

public class RegistrarClienteCommand implements OperacionCommand {
    private final ClienteDTO clienteDTO;
    private final NegocioAlquilerFacade facade;

    public RegistrarClienteCommand(ClienteDTO clienteDTO, NegocioAlquilerFacade facade) {
        this.clienteDTO = clienteDTO;
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.registrarCliente(clienteDTO);
    }
}
