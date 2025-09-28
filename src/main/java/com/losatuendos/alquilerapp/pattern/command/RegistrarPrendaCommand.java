package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;

public class RegistrarPrendaCommand implements OperacionCommand {
    private final PrendaDTO prendaDTO;
    private final NegocioAlquilerFacade facade;

    public RegistrarPrendaCommand(PrendaDTO prendaDTO, NegocioAlquilerFacade facade) {
        this.prendaDTO = prendaDTO;
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.registrarPrenda(prendaDTO);
    }
}
