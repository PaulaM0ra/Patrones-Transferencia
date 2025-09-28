package com.losatuendos.alquilerapp.factory;

import org.springframework.stereotype.Component;
import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.VestidoDama;

@Component("vestido")
public class VestidoFactory implements PrendaFactory {
    @Override
    public VestidoDama crearPrenda(PrendaDTO dto) {
        VestidoDama v = new VestidoDama();
        v.setRef(dto.getRef());
        v.setColor(dto.getColor());
        v.setMarca(dto.getMarca());
        v.setTalla(dto.getTalla());
        v.setValorAlquiler(dto.getValorAlquiler());
        v.setPedreria(dto.getPedreria());
        v.setLargo(dto.getLargo());
        v.setNumeroPiezas(dto.getNumeroPiezas());
        return v;
    }
}
