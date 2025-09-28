package com.losatuendos.alquilerapp.factory;

import org.springframework.stereotype.Component;
import com.losatuendos.alquilerapp.dto.PrendaDTO;
import com.losatuendos.alquilerapp.model.Disfraz;

@Component("disfraz")
public class DisfrazFactory implements PrendaFactory {
    @Override
    public Disfraz crearPrenda(PrendaDTO dto) {
        Disfraz d = new Disfraz();
        d.setRef(dto.getRef());
        d.setColor(dto.getColor());
        d.setMarca(dto.getMarca());
        d.setTalla(dto.getTalla());
        d.setValorAlquiler(dto.getValorAlquiler());
        d.setNombre(dto.getNombreDisfraz());
        return d;
    }
}