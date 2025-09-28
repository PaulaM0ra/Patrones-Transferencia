package com.losatuendos.alquilerapp.factory;

import org.springframework.stereotype.Component;
import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.TrajeCaballero;

@Component("traje")
public class TrajeFactory implements PrendaFactory {
    @Override
    public TrajeCaballero crearPrenda(PrendaDTO dto) {
        TrajeCaballero t = new TrajeCaballero();
        t.setRef(dto.getRef());
        t.setColor(dto.getColor());
        t.setMarca(dto.getMarca());
        t.setTalla(dto.getTalla());
        t.setValorAlquiler(dto.getValorAlquiler());
        t.setTipoTraje(dto.getTipoTraje());
        t.setAccesorio(dto.getAccesorio());
        return t;
    }
}
