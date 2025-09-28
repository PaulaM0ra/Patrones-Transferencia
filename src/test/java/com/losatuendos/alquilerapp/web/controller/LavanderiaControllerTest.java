package com.losatuendos.alquilerapp.web.controller;

import com.losatuendos.alquilerapp.model.Prenda;
import com.losatuendos.alquilerapp.model.VestidoDama;
import com.losatuendos.alquilerapp.pattern.PrendaComponent;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import com.losatuendos.alquilerapp.web.dto.EnvioLavanderiaRequest;
import com.losatuendos.alquilerapp.web.dto.LavanderiaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LavanderiaControllerTest {

    private NegocioAlquilerFacade facade;
    private LavanderiaController controller;

    @BeforeEach
    void setUp() {
        facade = Mockito.mock(NegocioAlquilerFacade.class);
        controller = new LavanderiaController(facade);
    }

    @Test
    void testRegistrar() {
        LavanderiaRequest req = new LavanderiaRequest();
        req.setRef("PR123");
        req.setPrioridad(true);

        ResponseEntity<Void> response = controller.registrar(req);

        verify(facade).registrarPrendaParaLavanderia("PR123", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCola() {
        PrendaComponent mockPrendaComponent = mock(PrendaComponent.class);
        List<PrendaComponent> lista = new ArrayList<>();
        lista.add(mockPrendaComponent);

        when(facade.listarPrendasParaLavanderia()).thenReturn(lista);

        ResponseEntity<List<PrendaComponent>> response = controller.cola();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(facade).listarPrendasParaLavanderia();
    }

    @Test
    void testEnviar() {
        List<Prenda> prendasEnviadas = new ArrayList<>();
        VestidoDama vestido1 = new VestidoDama();
        vestido1.setRef("V001");
        vestido1.setColor("Rojo");
        vestido1.setMarca("MarcaX");
        vestido1.setTalla("M");
        vestido1.setValorAlquiler(100.0);
        vestido1.setPedreria(true);
        vestido1.setLargo("Largo");
        vestido1.setNumeroPiezas(3);
        prendasEnviadas.add(vestido1);

        EnvioLavanderiaRequest req = new EnvioLavanderiaRequest();
        req.setCantidad(2);
        req.setTipoProceso("lavadoEstandar");

        when(facade.enviarPrendasALavanderia(2, "lavadoEstandar")).thenReturn(prendasEnviadas);

        ResponseEntity<List<Prenda>> response = controller.enviar(req);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0) instanceof VestidoDama);
        assertEquals("V001", response.getBody().get(0).getRef());

        verify(facade).enviarPrendasALavanderia(2, "lavadoEstandar");
    }
}
