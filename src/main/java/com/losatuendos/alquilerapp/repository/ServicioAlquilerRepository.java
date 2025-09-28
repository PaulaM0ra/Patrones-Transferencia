package com.losatuendos.alquilerapp.repository;

import com.losatuendos.alquilerapp.model.ServicioAlquiler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ServicioAlquilerRepository extends JpaRepository<ServicioAlquiler,Long> {

    List<ServicioAlquiler> findByClienteIdAndFechaAlquiAfterOrderByFechaAlqui(
            String clienteId, LocalDate fecha
    );

    List<ServicioAlquiler> findByFechaAlqui(LocalDate fechaAlqui);
}