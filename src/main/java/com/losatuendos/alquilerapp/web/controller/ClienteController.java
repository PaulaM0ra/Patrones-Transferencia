package com.losatuendos.alquilerapp.web.controller;

import com.losatuendos.alquilerapp.dto.ClienteDTO;
import com.losatuendos.alquilerapp.model.Cliente;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.ClienteRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final NegocioAlquilerFacade facade;

    // 📌 Patrón: Dependency Injection
    // Spring inyecta "NegocioAlquilerFacade" en el constructor,
    // desacoplando el controlador de la lógica de negocio.
    public ClienteController(NegocioAlquilerFacade facade) {
        this.facade = facade;
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody ClienteRequest req) {
        // 1) Copiar datos de Request a DTO
        ClienteDTO dto = new ClienteDTO();

        // 📌 Patrón: DTO (Data Transfer Object)
        // Se usa para trasladar datos entre capas de forma controlada.
        BeanUtils.copyProperties(req, dto);

        // 2) Llamar al método de fachada
        // 📌 Patrón: Facade (Fachada)
        // "NegocioAlquilerFacade" centraliza y simplifica el acceso a la lógica de negocio.
        Cliente c = facade.registrarCliente(dto);

        return ResponseEntity.ok(c);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable String id) {
        // 3) Llamar al método de consulta de la fachada
        // 📌 Patrón: Facade (Fachada) nuevamente
        Cliente c = facade.consultarCliente(id);
        return ResponseEntity.ok(c);
    }
}
