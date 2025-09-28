package com.losatuendos.alquilerapp.web.controller;

import com.losatuendos.alquilerapp.dto.EmpleadoDTO;
import com.losatuendos.alquilerapp.model.Empleado;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.EmpleadoRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final NegocioAlquilerFacade facade;

    public EmpleadoController(NegocioAlquilerFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<Empleado> crear(@RequestBody EmpleadoRequest req) {
        EmpleadoDTO dto = new EmpleadoDTO();
        BeanUtils.copyProperties(req, dto);
        Empleado e = facade.registrarEmpleado(dto);
        return ResponseEntity.ok(e);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> buscar(@PathVariable String id) {
        Empleado e = facade.consultarEmpleado(id);
        return ResponseEntity.ok(e);
    }
}

