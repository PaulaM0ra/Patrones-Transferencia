package com.losatuendos.alquilerapp.web.controller;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.ServicioAlquiler;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.AlquilerRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alquileres")
public class ServicioAlquilerController {

    private final NegocioAlquilerFacade facade;

    public ServicioAlquilerController(NegocioAlquilerFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<ServicioAlquiler> crear(@RequestBody AlquilerRequest req) {
        // convierte PrendaRequest → PrendaDTO
        List<PrendaDTO> dtos = req.getPrendas().stream()
                .map(r -> { var d = new PrendaDTO(); BeanUtils.copyProperties(r,d); return d; })
                .collect(Collectors.toList());

        ServicioAlquiler s = facade.crearServicioAlquiler(
                req.getClienteId(),
                req.getEmpleadoId(),
                dtos,
                req.getFechaAlqui()
        );
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioAlquiler> porId(@PathVariable long id) {
        return ResponseEntity.ok(facade.consultarServicioPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ServicioAlquiler>> porCliente(@PathVariable String clienteId) {
        return ResponseEntity.ok(facade.consultarServiciosPorCliente(clienteId));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ServicioAlquiler>> porFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(facade.consultarServiciosPorFecha(fecha));
    }
}

