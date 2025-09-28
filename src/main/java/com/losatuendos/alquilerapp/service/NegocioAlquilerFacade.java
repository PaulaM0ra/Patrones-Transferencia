package com.losatuendos.alquilerapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import com.losatuendos.alquilerapp.dto.ClienteDTO;
import com.losatuendos.alquilerapp.dto.EmpleadoDTO;
import com.losatuendos.alquilerapp.pattern.*;
import com.losatuendos.alquilerapp.pattern.observer.AdministradorLoggerObserver;
import com.losatuendos.alquilerapp.pattern.observer.ServicioAlquilerNotifier;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.id.ServicioAlquilerIDGenerator;
import com.losatuendos.alquilerapp.factory.PrendaFactory;
import com.losatuendos.alquilerapp.model.Cliente;
import com.losatuendos.alquilerapp.model.Empleado;
import com.losatuendos.alquilerapp.model.Prenda;
import com.losatuendos.alquilerapp.model.ServicioAlquiler;
import com.losatuendos.alquilerapp.repository.ClienteRepository;
import com.losatuendos.alquilerapp.repository.EmpleadoRepository;
import com.losatuendos.alquilerapp.repository.PrendaRepository;
import com.losatuendos.alquilerapp.repository.ServicioAlquilerRepository;

@Service
public class NegocioAlquilerFacade {

    private final ClienteRepository clienteRepo;
    private final EmpleadoRepository empleadoRepo;
    private final PrendaRepository prendaRepo;
    private final ServicioAlquilerRepository servicioRepo;
    private final ServicioAlquilerIDGenerator idGen;
    private final Map<String, PrendaFactory> factoryMap;
    Map<String, LavadoStrategy> lavadoImplMap;
    private final ServicioAlquilerNotifier notifier;

    /** Cola de lavandería en memoria */
    private final List<PrendaComponent> lavanderiaQueue = new ArrayList<>();

    public NegocioAlquilerFacade(
            ClienteRepository clienteRepo,
            EmpleadoRepository empleadoRepo,
            PrendaRepository prendaRepo,
            ServicioAlquilerRepository servicioRepo,
            ServicioAlquilerIDGenerator idGen,
            Map<String, PrendaFactory> factoryMap,
            Map<String, LavadoStrategy> lavadoImplMap,
            ServicioAlquilerNotifier notifier,
            AdministradorLoggerObserver administradorLoggerObserver
    ) {
        this.clienteRepo = clienteRepo;
        this.empleadoRepo = empleadoRepo;
        this.prendaRepo = prendaRepo;
        this.servicioRepo = servicioRepo;
        this.idGen = idGen;
        this.factoryMap = factoryMap;
        this.lavadoImplMap    = lavadoImplMap;
        this.notifier = notifier;
        this.notifier.subscribe(administradorLoggerObserver);
    }

    //========================================
    // 1) Registro de prendas, clientes y empleados
    //========================================

    public Prenda registrarPrenda(PrendaDTO dto) {
        PrendaFactory f = factoryMap
                .get(dto.getTipoDiscriminador());
        if (f == null) {
            throw new IllegalArgumentException(
                    "Tipo de prenda desconocido: " + dto.getTipoDiscriminador());
        }
        Prenda p = f.crearPrenda(dto);
        return prendaRepo.save(p);
    }

    public Cliente registrarCliente(ClienteDTO dto) {
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNombre(dto.getNombre());
        c.setDireccion(dto.getDireccion());
        c.setTelefono(dto.getTelefono());
        c.setMail(dto.getMail());
        return clienteRepo.save(c);
    }

    public Cliente consultarCliente(String id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no existe: " + id));
    }

    public Empleado registrarEmpleado(EmpleadoDTO dto) {
        Empleado e = new Empleado();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setDireccion(dto.getDireccion());
        e.setTelefono(dto.getTelefono());
        e.setCargo(dto.getCargo());
        return empleadoRepo.save(e);
    }

    public Empleado consultarEmpleado(String id) {
        return empleadoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no existe: " + id));
    }

    //========================================
    // 2) Registro de servicio de alquiler
    //========================================

    /**
     * Crea un nuevo servicio de alquiler:
     * 1) Valida existencia de cliente y empleado.
     * 2) Crea y persiste cada prenda usando la fábrica adecuada.
     * 3) Genera un ID único.
     * 4) Construye y guarda la entidad ServicioAlquiler.
     */
    @Transactional
    public ServicioAlquiler crearServicioAlquiler(
            String clienteId,
            String empleadoId,
            List<PrendaDTO> dtos,
            LocalDate fechaAlqui
    ) {
        // 1) Buscar y validar cliente y empleado
        Cliente cliente = clienteRepo.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no existe"));

        // 2) Crear y guardar cada prenda con su fábrica
        List<Prenda> prendas = dtos.stream()
                .map(dto -> {
                    PrendaFactory factory = factoryMap.get(dto.getTipoDiscriminador());
                    if (factory == null) {
                        throw new IllegalArgumentException(
                                "Tipo de prenda desconocido: " + dto.getTipoDiscriminador()
                        );
                    }
                    Prenda p = factory.crearPrenda(dto);
                    return prendaRepo.save(p);
                })
                .collect(Collectors.toList());

        // 3) Obtener nuevo ID
        long nuevoId = idGen.getNextId();

        // 4) Construir y persistir el servicio
        ServicioAlquiler servicio = new ServicioAlquiler();
        servicio.setId(nuevoId);
        servicio.setCliente(cliente);
        servicio.setEmpleado(empleado);
        servicio.setFechaSolic(LocalDate.now());
        servicio.setFechaAlqui(fechaAlqui);
        servicio.setPrendas(prendas);

        ServicioAlquiler savedServicio = servicioRepo.save(servicio);
        this.notifier.notify("NUEVO_ALQUILER", savedServicio);

        return savedServicio;
    }

    //========================================
    // 3) Consultas de servicio alquiler
    //========================================

    public ServicioAlquiler consultarServicioPorId(long id) {
        return servicioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Servicio no encontrado: " + id));
    }

    //========================================
    // 4) Consultas de servicio por Cliente
    //========================================
    public List<ServicioAlquiler> consultarServiciosPorCliente(
            String clienteId
    ) {
        LocalDate hoy = LocalDate.now();
        return servicioRepo.findByClienteIdAndFechaAlquiAfterOrderByFechaAlqui(
                clienteId, hoy
        );
    }

    //========================================
    // 5) Consultas de servicio por Fecha de alquiler
    //========================================
    public List<ServicioAlquiler> consultarServiciosPorFecha(
            LocalDate fechaAlqui
    ) {
        return servicioRepo.findByFechaAlqui(fechaAlqui);
    }

    //========================================
    // 6) Consulta de prenda por talla
    //========================================
    public List<Prenda> consultarPrendasPorTalla(String talla) {
        return prendaRepo.findByTalla(talla);
    }

    //========================================
    // 7) Registro prenda para envio a lavanderia
    //========================================

    /** Registra una prenda en la cola de lavandería */
    public void registrarPrendaParaLavanderia(
            String ref, boolean prioridad
    ) {
        Prenda prenda = prendaRepo.findById(ref)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prenda no existe: " + ref));
        PrendaComponent leaf = new PrendaLeaf(prenda);
        PrendaComponent comp = prioridad
                ? new PrendaPrioritaria(leaf)
                : leaf;
        lavanderiaQueue.add(comp);
    }

    //========================================
    // 8) Visualizacion de listado de prendas para envio a lavanderia
    //========================================

    /** Devuelve todas las prendas en cola (sin enviarlas) */
    public List<PrendaComponent> listarPrendasParaLavanderia() {
        return List.copyOf(lavanderiaQueue);
    }

    //========================================
    // 9) Envia prendas a lavanderia
    //========================================

    /**
     * Envía N prendas a lavandería usando el tipo de proceso ('estandar' o 'urgente'),
     * delega en el Bridge y Adapter, y las quita de la cola.
     */
    public List<Prenda> enviarPrendasALavanderia(
            int cantidad, String tipoProceso
    ) {
        LavadoStrategy impl = lavadoImplMap.get(tipoProceso);
        if (impl == null) {
            throw new IllegalArgumentException(
                    "Proceso de lavado desconocido: " + tipoProceso);
        }

        // 1) Armar el lote con las primeras N
        LotePrendas lote = new LotePrendas();
        int toIndex = Math.min(cantidad, lavanderiaQueue.size());
        for (int i = 0; i < toIndex; i++) {
            lote.add(lavanderiaQueue.get(i));
        }

        // 2) Ejecutar el lavado (Bridge + Adapter)
        impl.lavar(lote);

        // 3) Quitar de la cola
        List<PrendaComponent> componentesEnviados = new ArrayList<>(
                lavanderiaQueue.subList(0, toIndex)
        );
        lavanderiaQueue.subList(0, toIndex).clear();

        // 4) Devolver la lista de Prenda (extraída de los componentes)
//        return enviados.stream()
//                .map(comp -> {
//                    if (comp instanceof PrendaLeaf leaf) {
//                        return leaf.getPrenda();
//                    } else {
//                        // unwrap decorator
//                        PrendaComponent unwrapped = ((PrendaDecorator) comp).getWrappee();
//                        return ((PrendaLeaf) unwrapped).getPrenda();
//                    }
//                })
//                .collect(Collectors.toList());
        List<Prenda> prendasEnviadas = componentesEnviados.stream()
                .map(comp -> {
                    if (comp instanceof PrendaLeaf leaf) {
                        return leaf.getPrenda();
                    } else {
//                         unwrap decorator
                        PrendaComponent unwrapped = ((PrendaDecorator) comp).getWrappee();
                        return ((PrendaLeaf) unwrapped).getPrenda();
                    }
                })
                .collect(Collectors.toList());

        this.notifier.notify("LAVANDERIA_COMPLETADA", prendasEnviadas);
        return prendasEnviadas;
    }

}
