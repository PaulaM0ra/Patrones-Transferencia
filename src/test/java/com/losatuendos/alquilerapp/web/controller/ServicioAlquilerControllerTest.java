package com.losatuendos.alquilerapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losatuendos.alquilerapp.model.Cliente;
import com.losatuendos.alquilerapp.model.Empleado;
import com.losatuendos.alquilerapp.model.ServicioAlquiler;
import com.losatuendos.alquilerapp.web.dto.AlquilerRequest;
import com.losatuendos.alquilerapp.web.dto.PrendaRequest;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicioAlquilerController.class)
public class ServicioAlquilerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NegocioAlquilerFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearServicioAlquiler() throws Exception {
        PrendaRequest prendaReq = new PrendaRequest();
        prendaReq.setRef("V001");
        prendaReq.setTalla("M");
        prendaReq.setValorAlquiler(120.0);
        prendaReq.setColor("Rojo");
        prendaReq.setMarca("MarcaX");

        AlquilerRequest request = new AlquilerRequest();
        request.setClienteId("C123");
        request.setEmpleadoId("E456");
        request.setFechaAlqui(LocalDate.of(2025, 5, 20));
        request.setPrendas(List.of(prendaReq));

        Cliente cliente = new Cliente();
        cliente.setId("C123");

        Empleado empleado = new Empleado();
        empleado.setId("E456");

        ServicioAlquiler mockServicio = new ServicioAlquiler();
        mockServicio.setCliente(cliente);
        mockServicio.setEmpleado(empleado);
        mockServicio.setFechaAlqui(LocalDate.of(2025, 5, 20));

        when(facade.crearServicioAlquiler(
                eq("C123"), eq("E456"),
                anyList(),
                eq(LocalDate.of(2025, 5, 20))
        )).thenReturn(mockServicio);

        mockMvc.perform(post("/api/alquileres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente.id").value("C123"))
                .andExpect(jsonPath("$.empleado.id").value("E456"))
                .andExpect(jsonPath("$.fechaAlqui").value("2025-05-20"));
    }

    @Test
    void testConsultarPorId() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId("C321");

        Empleado empleado = new Empleado();
        empleado.setId("E654");

        ServicioAlquiler mockServicio = new ServicioAlquiler();
        mockServicio.setCliente(cliente);
        mockServicio.setEmpleado(empleado);
        mockServicio.setFechaAlqui(LocalDate.of(2025, 5, 18));

        when(facade.consultarServicioPorId(1L)).thenReturn(mockServicio);

        mockMvc.perform(get("/api/alquileres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente.id").value("C321"))
                .andExpect(jsonPath("$.empleado.id").value("E654"))
                .andExpect(jsonPath("$.fechaAlqui").value("2025-05-18"));
    }

    @Test
    void testConsultarPorCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId("C999");

        ServicioAlquiler mockServicio = new ServicioAlquiler();
        mockServicio.setCliente(cliente);
        mockServicio.setFechaAlqui(LocalDate.of(2025, 5, 17));

        when(facade.consultarServiciosPorCliente("C999")).thenReturn(List.of(mockServicio));

        mockMvc.perform(get("/api/alquileres/cliente/C999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cliente.id").value("C999"))
                .andExpect(jsonPath("$[0].fechaAlqui").value("2025-05-17"));
    }

    @Test
    void testConsultarPorFecha() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId("C555");

        ServicioAlquiler mockServicio = new ServicioAlquiler();
        mockServicio.setFechaAlqui(LocalDate.of(2025, 5, 19));
        mockServicio.setCliente(cliente);

        when(facade.consultarServiciosPorFecha(LocalDate.of(2025, 5, 19))).thenReturn(List.of(mockServicio));

        mockMvc.perform(get("/api/alquileres/fecha/2025-05-19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fechaAlqui").value("2025-05-19"))
                .andExpect(jsonPath("$[0].cliente.id").value("C555"));
    }
}
