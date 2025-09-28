package com.losatuendos.alquilerapp.pattern.command;

import com.losatuendos.alquilerapp.dto.ClienteDTO;
import com.losatuendos.alquilerapp.service.NegocioAlquilerFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * Pruebas conceptuales para el Command Pattern.
 * Estos tests verifican que los objetos Command invocan correctamente
 * los métodos correspondientes en el NegocioAlquilerFacade.
 */
@ExtendWith(MockitoExtension.class)
public class CommandPatternTest {

    @Mock
    private NegocioAlquilerFacade facadeMock;

    /**
     * Test para RegistrarClienteCommand.
     * Verifica que al ejecutar el comando, se llama al método
     * registrarCliente del facade con el DTO correcto.
     */
    @Test
    void testRegistrarClienteCommand_DeberiaLlamarFacade() {
        // --- Mock Setup ---
        // 1. NegocioAlquilerFacade ya está mockeado usando @Mock.

        // --- Test Data ---
        // 2. Crear un ClienteDTO de prueba.
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId("C001");
        clienteDTO.setNombre("Cliente de Prueba");

        // --- Command Creation ---
        // 3. Crear una instancia de RegistrarClienteCommand con el DTO y el facade mockeado.
        RegistrarClienteCommand command = new RegistrarClienteCommand(clienteDTO, facadeMock);

        // --- Execution ---
        // 4. Ejecutar el comando.
        command.execute();

        // --- Verification ---
        // 5. Verificar que el método facade.registrarCliente(clienteDTO) fue llamado
        //    exactamente una vez y con el DTO correcto.
        verify(facadeMock, times(1)).registrarCliente(clienteDTO);
    }

    // Conceptualmente, se añadirían tests similares para otras implementaciones de Command:
    // - testRegistrarPrendaCommand_DeberiaLlamarFacade()
    // - testCrearServicioAlquilerCommand_DeberiaLlamarFacade()
    // - testRegistrarPrendaParaLavanderiaCommand_DeberiaLlamarFacade()
    // - testEnviarPrendasALavanderiaCommand_DeberiaLlamarFacade()
    // Cada uno verificaría que el método 'execute' del comando respectivo
    // invoca el método correspondiente en NegocioAlquilerFacade con los parámetros correctos.
}