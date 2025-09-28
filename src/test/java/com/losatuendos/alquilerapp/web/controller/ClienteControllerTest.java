package com.losatuendos.alquilerapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losatuendos.alquilerapp.model.Cliente;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.ClienteRequest;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NegocioAlquilerFacade facade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearCliente() throws Exception {
        ClienteRequest request = new ClienteRequest();
        request.setId("C123");
        request.setNombre("Nombre Cliente");
        request.setDireccion("Dirección Genérica 123");
        request.setTelefono("3000000000");
        request.setMail("cliente@correo.com");

        Cliente clienteEsperado = new Cliente();
        clienteEsperado.setId("C123");
        clienteEsperado.setNombre("Nombre Cliente");
        clienteEsperado.setDireccion("Dirección Genérica 123");
        clienteEsperado.setTelefono("3000000000");
        clienteEsperado.setMail("cliente@correo.com");

        when(facade.registrarCliente(any())).thenReturn(clienteEsperado);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("C123"))
                .andExpect(jsonPath("$.nombre").value("Nombre Cliente"))
                .andExpect(jsonPath("$.mail").value("cliente@correo.com"));
    }

    @Test
    void testBuscarCliente() throws Exception {
        // Cliente esperado al buscar por ID
        Cliente clienteEsperado = new Cliente();
        clienteEsperado.setId("C123");
        clienteEsperado.setNombre("Nombre Cliente");
        clienteEsperado.setDireccion("Dirección Genérica 123");
        clienteEsperado.setTelefono("3000000000");
        clienteEsperado.setMail("cliente@correo.com");

        // Simular respuesta de la fachada
        when(facade.consultarCliente("C123")).thenReturn(clienteEsperado);

        // Realizar la petición GET
        mockMvc.perform(get("/api/clientes/C123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("C123"))
                .andExpect(jsonPath("$.nombre").value("Nombre Cliente"))
                .andExpect(jsonPath("$.direccion").value("Dirección Genérica 123"))
                .andExpect(jsonPath("$.telefono").value("3000000000"))
                .andExpect(jsonPath("$.mail").value("cliente@correo.com"));
    }

}
