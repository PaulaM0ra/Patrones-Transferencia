package com.losatuendos.alquilerapp.web.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.Prenda;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.PrendaRequest;

import java.util.List;

@RestController
@RequestMapping("/api/prendas")
public class PrendaController {

    private final NegocioAlquilerFacade facade;

    public PrendaController(NegocioAlquilerFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<Prenda> crearPrenda(@RequestBody PrendaRequest req) {
        PrendaDTO dto = new PrendaDTO();
        BeanUtils.copyProperties(req, dto);
        Prenda p = facade.registrarPrenda(dto);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/talla/{talla}")
    public ResponseEntity<List<Prenda>> porTalla(@PathVariable String talla) {
        return ResponseEntity.ok(facade.consultarPrendasPorTalla(talla));
    }
}
