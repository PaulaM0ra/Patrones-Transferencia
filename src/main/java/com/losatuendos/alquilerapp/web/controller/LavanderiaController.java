package com.losatuendos.alquilerapp.web.controller;

import com.losatuendos.alquilerapp.model.Prenda;
import com.losatuendos.alquilerapp.pattern.PrendaComponent;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.EnvioLavanderiaRequest;
import com.losatuendos.alquilerapp.web.dto.LavanderiaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/lavanderia")
public class LavanderiaController {

    private final NegocioAlquilerFacade facade;

    public LavanderiaController(NegocioAlquilerFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(
            @RequestBody LavanderiaRequest req
    ) {
        facade.registrarPrendaParaLavanderia(req.getRef(), req.isPrioridad());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cola")
    public ResponseEntity<List<PrendaComponent>> cola() {
        return ResponseEntity.ok(facade.listarPrendasParaLavanderia());
    }

    @PostMapping("/enviar")
    public ResponseEntity<List<Prenda>> enviar(
            @RequestBody EnvioLavanderiaRequest req
    ) {
        return ResponseEntity.ok(
                facade.enviarPrendasALavanderia(req.getCantidad(), req.getTipoProceso())
        );
    }
}

