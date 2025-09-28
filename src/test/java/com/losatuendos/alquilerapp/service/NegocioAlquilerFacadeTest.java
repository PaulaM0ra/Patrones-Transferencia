package com.losatuendos.alquilerapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.factory.PrendaFactory;
import com.losatuendos.alquilerapp.id.ServicioAlquilerIDGenerator;
import com.losatuendos.alquilerapp.model.Cliente;
import com.losatuendos.alquilerapp.model.Empleado;
import com.losatuendos.alquilerapp.model.Prenda;
import com.losatuendos.alquilerapp.model.ServicioAlquiler;
import com.losatuendos.alquilerapp.model.VestidoDama;
import com.losatuendos.alquilerapp.pattern.LavadoEstandar;
import com.losatuendos.alquilerapp.pattern.LavadoStrategy;
import com.losatuendos.alquilerapp.pattern.LavadoUrgente;
import com.losatuendos.alquilerapp.pattern.LotePrendas;
import com.losatuendos.alquilerapp.pattern.PrendaLeaf;
import com.losatuendos.alquilerapp.pattern.observer.AdministradorLoggerObserver;
import com.losatuendos.alquilerapp.pattern.observer.ServicioAlquilerNotifier;
import com.losatuendos.alquilerapp.repository.ClienteRepository;
import com.losatuendos.alquilerapp.repository.EmpleadoRepository;
import com.losatuendos.alquilerapp.repository.PrendaRepository;
import com.losatuendos.alquilerapp.repository.ServicioAlquilerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NegocioAlquilerFacadeTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private PrendaRepository prendaRepository;

    @Mock
    private ServicioAlquilerRepository servicioAlquilerRepository;

    @Mock
    private ServicioAlquilerIDGenerator idGenerator;

    @Mock
    private LavadoEstandar lavadoEstandarMock;

    @Mock
    private LavadoUrgente lavadoUrgenteMock;

    @Mock
    private ServicioAlquilerNotifier notifierMock;

    @Mock
    private AdministradorLoggerObserver observerMock;

    private NegocioAlquilerFacade facade;

    private final PrendaFactory prendaFactory = new PrendaFactory() {
        @Override
        public Prenda crearPrenda(PrendaDTO dto) {
            VestidoDama vestido = new VestidoDama();
            vestido.setRef(dto.getRef());
            vestido.setColor(dto.getColor());
            vestido.setMarca(dto.getMarca());
            vestido.setTalla(dto.getTalla());
            vestido.setValorAlquiler(dto.getValorAlquiler());
            vestido.setPedreria(false);
            vestido.setLargo("Mediano");
            vestido.setNumeroPiezas(1);
            return vestido;
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, PrendaFactory> factoryMap = Map.of("VestidoDama", prendaFactory);
        Map<String, LavadoStrategy> lavadoMap = Map.of( // Prepare the map of LavadoStrategy mocks
                "estandar", lavadoEstandarMock,
                "urgente", lavadoUrgenteMock
        );

        // Initialize NegocioAlquilerFacade with all mocks
        facade = new NegocioAlquilerFacade(
                clienteRepository,
                empleadoRepository,
                prendaRepository,
                servicioAlquilerRepository,
                idGenerator,
                factoryMap,
                lavadoMap,
                notifierMock, // Added for Observer pattern
                observerMock  // Added for Observer pattern
        );
    }

    @Test
    void crearServicioAlquiler_UsandoVestidoDama() {
        String clienteId = "cliente1";
        String empleadoId = "empleado1";
        LocalDate fechaAlquiler = LocalDate.now();

        PrendaDTO dto = new PrendaDTO();
        dto.setTipoDiscriminador("VestidoDama");
        dto.setRef("V001");
        dto.setColor("Rojo");
        dto.setMarca("MarcaX");
        dto.setTalla("M");
        dto.setValorAlquiler(150.0);

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Empleado empleado = new Empleado();
        empleado.setId(empleadoId);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));
        when(prendaRepository.save(any(VestidoDama.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(idGenerator.getNextId()).thenReturn(123L);
        when(servicioAlquilerRepository.save(any(ServicioAlquiler.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServicioAlquiler servicio = facade.crearServicioAlquiler(clienteId, empleadoId, List.of(dto), fechaAlquiler);

        assertNotNull(servicio);
        assertEquals(123L, servicio.getId());
        assertEquals(cliente, servicio.getCliente());
        assertEquals(empleado, servicio.getEmpleado());
        assertEquals(fechaAlquiler, servicio.getFechaAlqui());
        assertEquals(1, servicio.getPrendas().size());

        Prenda prenda = servicio.getPrendas().get(0);
        assertTrue(prenda instanceof VestidoDama);

        VestidoDama vestido = (VestidoDama) prenda;
        assertEquals("V001", vestido.getRef());
        assertEquals("Rojo", vestido.getColor());
        assertEquals("MarcaX", vestido.getMarca());
        assertEquals("M", vestido.getTalla());
        assertEquals(150.0, vestido.getValorAlquiler());

        verify(clienteRepository).findById(clienteId);
        verify(empleadoRepository).findById(empleadoId);
        verify(prendaRepository).save(prenda);
        verify(servicioAlquilerRepository).save(any(ServicioAlquiler.class));
        verify(idGenerator).getNextId();
        verify(notifierMock).notify(eq("NUEVO_ALQUILER"), any(ServicioAlquiler.class));
    }

    /**
     * Test para verificar el funcionamiento del Strategy Pattern en el envío de prendas a lavandería.
     * Se asegura de que se llame a la implementación correcta de LavadoStrategy (estandar o urgente)
     * según el tipo de proceso especificado.
     */
    @Test
    void testEnviarPrendasALavanderia_DeberiaUsarStrategyCorrecto() {
        // --- Mock Setup ---
        // 1. Mock PrendaRepository para devolver una prenda de prueba.
        Prenda prendaMock = new VestidoDama(); // Usar una instancia real o un mock de Prenda
        prendaMock.setRef("P001");
        when(prendaRepository.findById("P001")).thenReturn(Optional.of(prendaMock));

        // --- Execution ---
        // 2. Registrar una prenda para lavandería.
        // Esto la añade a la lavanderiaQueue interna de NegocioAlquilerFacade.
        facade.registrarPrendaParaLavanderia("P001", false); // false para no prioritaria

        // 3. Enviar a lavandería con proceso "estandar".
        facade.enviarPrendasALavanderia(1, "estandar");

        // --- Verification ---
        // 4. Verificar que el método lavar() del LavadoEstandar (mock) fue llamado.
        // Se espera que el facade, al recibir "estandar", seleccione el LavadoEstandar mockeado
        // del mapa 'lavadoImplMap' y ejecute su método 'lavar'.
        verify(lavadoEstandarMock).lavar(any(LotePrendas.class));
        verify(lavadoUrgenteMock, never()).lavar(any(LotePrendas.class)); // Asegurar que el urgente no fue llamado

        // Resetear mocks para la siguiente verificación si es necesario o usar una nueva prenda
        // clearInvocations(lavadoEstandarMock); // Si se reutiliza la misma prenda y cola

        // --- Execution for "urgente" ---
        // 5. Registrar otra prenda (o la misma si la cola se vacía y se resetean mocks).
        // Para este ejemplo, asumimos que la prenda anterior fue procesada y la cola está vacía o
        // registramos una nueva para asegurar que la prueba es limpia.
        // Aquí, reutilizaremos la lógica de la cola, asumiendo que la prenda anterior fue "enviada".
        // Si la cola no se vaciara automáticamente, necesitaríamos más prendas o limpiar la cola.
        // Por simplicidad, vamos a registrarla de nuevo.
        facade.registrarPrendaParaLavanderia("P001", true); // true para prioritaria

        // 6. Enviar a lavandería con proceso "urgente".
        facade.enviarPrendasALavanderia(1, "urgente");

        // --- Verification for "urgente" ---
        // 7. Verificar que el método lavar() del LavadoUrgente (mock) fue llamado.
        verify(lavadoUrgenteMock).lavar(any(LotePrendas.class));
        // Verificar que el estandar no fue llamado esta vez (si se reseteó o es una nueva interacción)
        // Si no se reseteó, el estandar ya tendría una invocación.
        // Para ser precisos, podríamos contar las invocaciones exactas: verify(lavadoEstandarMock, times(1)).lavar(any());
        // y verify(lavadoUrgenteMock, times(1)).lavar(any());
    }

    /**
     * Test para verificar el funcionamiento del Observer Pattern al crear un servicio de alquiler.
     * Se asegura de que el ServicioAlquilerNotifier.notify() sea llamado correctamente
     * después de que un nuevo servicio de alquiler es creado y guardado.
     */
    @Test
    void testCrearServicioAlquiler_DeberiaNotificarObserver() {
        // --- Mock Setup ---
        // 1. Mocks para ClienteRepository, EmpleadoRepository, PrendaRepository,
        //    ServicioAlquilerRepository, ServicioAlquilerIDGenerator, y notifierMock
        //    ya están configurados en el método setUp().
        //    PrendaFactory también se configura en setUp.

        // --- Test Data ---
        // 2. Definir datos de prueba para clienteId, empleadoId, DTOs de prenda y fecha de alquiler.
        String clienteId = "clienteTestObs";
        String empleadoId = "empleadoTestObs";
        LocalDate fechaAlquiler = LocalDate.now().plusDays(5);

        PrendaDTO prendaDto = new PrendaDTO();
        prendaDto.setTipoDiscriminador("VestidoDama"); // Asegurar que coincida con la factoryMap
        prendaDto.setRef("V002Obs");
        prendaDto.setColor("Azul");
        prendaDto.setMarca("MarcaObs");
        prendaDto.setTalla("S");
        prendaDto.setValorAlquiler(120.0);
        List<PrendaDTO> dtos = List.of(prendaDto);

        Cliente clienteMock = new Cliente();
        clienteMock.setId(clienteId);
        Empleado empleadoMock = new Empleado();
        empleadoMock.setId(empleadoId);

        // Configurar Prenda y ServicioAlquiler que se espera que se creen y guarden
        VestidoDama prendaCreadaMock = new VestidoDama(); // Coincide con la PrendaFactory
        prendaCreadaMock.setRef(prendaDto.getRef());

        ServicioAlquiler servicioCreadoMock = new ServicioAlquiler();
        servicioCreadoMock.setId(124L); // ID de ejemplo
        servicioCreadoMock.setCliente(clienteMock);
        servicioCreadoMock.setEmpleado(empleadoMock);
        servicioCreadoMock.setPrendas(List.of(prendaCreadaMock));


        // 3. Configurar los mocks para que devuelvan los objetos de prueba cuando se les llame.
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteMock));
        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleadoMock));
        // La PrendaFactory real (anónima) creará la instancia, el repo la guarda.
        when(prendaRepository.save(any(Prenda.class))).thenReturn(prendaCreadaMock);
        when(idGenerator.getNextId()).thenReturn(124L);
        // El mock de servicioAlquilerRepository debe devolver el servicio que se espera sea notificado.
        when(servicioAlquilerRepository.save(any(ServicioAlquiler.class))).thenReturn(servicioCreadoMock);

        // --- Execution ---
        // 4. Llamar al método facade.crearServicioAlquiler(...)
        // El facade está configurado con notifierMock en setUp().
        ServicioAlquiler resultadoServicio = facade.crearServicioAlquiler(clienteId, empleadoId, dtos, fechaAlquiler);

        // --- Verification ---
        // 5. Verificar que notifierMock.notify("NUEVO_ALQUILER", servicioCreado) fue llamado.
        //    Se usa eq() para el string del tipo de evento y el objeto exacto (servicioCreadoMock)
        //    que se configuró para ser devuelto por servicioAlquilerRepository.save().
        verify(notifierMock, times(1)).notify(eq("NUEVO_ALQUILER"), eq(servicioCreadoMock));

        // Adicionalmente, verificar que el servicio devuelto es el esperado.
        assertNotNull(resultadoServicio);
        assertEquals(servicioCreadoMock.getId(), resultadoServicio.getId());
    }
}
