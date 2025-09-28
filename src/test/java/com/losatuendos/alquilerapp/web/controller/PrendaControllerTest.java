package com.losatuendos.alquilerapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.losatuendos.alquilerapp.model.Prenda;

import com.losatuendos.alquilerapp.model.VestidoDama;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.PrendaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PrendaController.class)
public class PrendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NegocioAlquilerFacade facade;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCrearPrendaVestidoDama() throws Exception {
        PrendaRequest prendaRequest = new PrendaRequest();
        prendaRequest.setRef("REF001");
        prendaRequest.setColor("Rojo");
        prendaRequest.setMarca("Zara");
        prendaRequest.setTalla("M");
        prendaRequest.setValorAlquiler(50000.0);
        prendaRequest.setPedreria(true);
        prendaRequest.setLargo("Largo");
        prendaRequest.setNumeroPiezas(1);
        prendaRequest.setTipoDiscriminador("vestido");

        VestidoDama expectedPrenda = new VestidoDama();
        expectedPrenda.setRef("REF001");
        expectedPrenda.setColor("Rojo");
        expectedPrenda.setMarca("Zara");
        expectedPrenda.setTalla("M");
        expectedPrenda.setValorAlquiler(50000.0);
        expectedPrenda.setPedreria(true);
        expectedPrenda.setLargo("Largo");
        expectedPrenda.setNumeroPiezas(1);

        when(facade.registrarPrenda(any())).thenReturn(expectedPrenda);

        mockMvc.perform(post("/api/prendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prendaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ref").value("REF001"))
                .andExpect(jsonPath("$.color").value("Rojo"))
                .andExpect(jsonPath("$.marca").value("Zara"))
                .andExpect(jsonPath("$.talla").value("M"))
                .andExpect(jsonPath("$.valorAlquiler").value(50000.0))
                .andExpect(jsonPath("$.pedreria").value(true))
                .andExpect(jsonPath("$.largo").value("Largo"))
                .andExpect(jsonPath("$.numeroPiezas").value(1));
    }
    @Test
    public void testConsultarPrendasPorTalla() throws Exception {
        VestidoDama vestido = new VestidoDama();
        vestido.setRef("V002");
        vestido.setColor("Azul");
        vestido.setMarca("H&M");
        vestido.setTalla("S");
        vestido.setValorAlquiler(40000.0);
        vestido.setPedreria(false);
        vestido.setLargo("Corto");
        vestido.setNumeroPiezas(2);

        List<Prenda> lista = List.of(vestido);

        when(facade.consultarPrendasPorTalla("S")).thenReturn(lista);

        mockMvc.perform(get("/api/prendas/talla/S")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].ref").value("V002"))
                .andExpect(jsonPath("$[0].talla").value("S"))
                .andExpect(jsonPath("$[0].color").value("Azul"));
    }

}
