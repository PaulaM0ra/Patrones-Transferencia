package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;

public class EnviarPrendasALavanderiaCommand implements OperacionCommand {
    private final int cantidad;
    private final String tipoProceso;
    private final NegocioAlquilerFacade facade;

    public EnviarPrendasALavanderiaCommand(int cantidad, String tipoProceso, NegocioAlquilerFacade facade) {
        this.cantidad = cantidad;
        this.tipoProceso = tipoProceso;
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.enviarPrendasALavanderia(cantidad, tipoProceso);
    }
}