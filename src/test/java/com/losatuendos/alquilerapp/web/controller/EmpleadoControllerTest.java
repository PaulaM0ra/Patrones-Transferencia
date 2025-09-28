package com.losatuendos.alquilerapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losatuendos.alquilerapp.model.Empleado;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.EmpleadoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NegocioAlquilerFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearEmpleado() throws Exception {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setId("E001");
        request.setNombre("Carlos López");
        request.setDireccion("Carrera 45 #23");
        request.setTelefono("3001234567");
        request.setCargo("Administrador");

        Empleado empleadoEsperado = new Empleado();
        empleadoEsperado.setId("E001");
        empleadoEsperado.setNombre("Carlos López");
        empleadoEsperado.setDireccion("Carrera 45 #23");
        empleadoEsperado.setTelefono("3001234567");
        empleadoEsperado.setCargo("Administrador");

        when(facade.registrarEmpleado(any())).thenReturn(empleadoEsperado);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("E001"))
                .andExpect(jsonPath("$.nombre").value("Carlos López"))
                .andExpect(jsonPath("$.cargo").value("Administrador"));
    }

    @Test
    void testBuscarEmpleado() throws Exception {
        Empleado empleadoEsperado = new Empleado();
        empleadoEsperado.setId("E001");
        empleadoEsperado.setNombre("Carlos López");
        empleadoEsperado.setDireccion("Carrera 45 #23");
        empleadoEsperado.setTelefono("3001234567");
        empleadoEsperado.setCargo("Administrador");

        when(facade.consultarEmpleado("E001")).thenReturn(empleadoEsperado);

        mockMvc.perform(get("/api/empleados/E001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("E001"))
                .andExpect(jsonPath("$.nombre").value("Carlos López"))
                .andExpect(jsonPath("$.telefono").value("3001234567"));
    }
}
