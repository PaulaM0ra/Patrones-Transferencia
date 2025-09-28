package com.losatuendos.alquilerapp.repository;

import com.losatuendos.alquilerapp.model.Prenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrendaRepository extends JpaRepository<Prenda,String> {

    List<Prenda> findByTalla(String talla);
}
