package com.losatuendos.alquilerapp.pattern;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

//Bridge pattern
@Service
public class ProcesoLavado {
    private final LavadoStrategy impl;
    public ProcesoLavado(@Qualifier("estandar") LavadoStrategy impl){
        this.impl = impl;
    }
    public void lavar(LotePrendas lote){
        impl.lavar(lote);
    }
}
