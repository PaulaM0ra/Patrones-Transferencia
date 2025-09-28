package com.losatuendos.alquilerapp.pattern;

import org.springframework.stereotype.Component;

//Patron Bridge y Adapter
@Component("estandar")
public class LavadoEstandar implements LavadoStrategy {
    @Override public void lavar(LotePrendas lote){ /* envío normal */ }
}
