package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;

public class RegistrarPrendaParaLavanderiaCommand implements OperacionCommand {
    private final String ref;
    private final boolean prioridad;
    private final NegocioAlquilerFacade facade;

    public RegistrarPrendaParaLavanderiaCommand(String ref, boolean prioridad, NegocioAlquilerFacade facade) {
        this.ref = ref;
        this.prioridad = prioridad;
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.registrarPrendaParaLavanderia(ref, prioridad);
    }
}
