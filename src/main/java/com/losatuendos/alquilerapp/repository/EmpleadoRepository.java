package com.losatuendos.alquilerapp.repository;

import com.losatuendos.alquilerapp.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado,String> {

}
