package com.losatuendos.alquilerapp.factory;

import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.Prenda;

public interface PrendaFactory {
    Prenda crearPrenda(PrendaDTO dto);
}