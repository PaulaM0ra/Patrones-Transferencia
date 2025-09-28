package com.losatuendos.alquilerapp.pattern;

import org.springframework.stereotype.Component;

@Component
public class LavanderiaExternaAdapter implements LavadoStrategy {
    private final LavanderiaExterna external = new LavanderiaExterna();
    @Override
    public void lavar(LotePrendas lote){
        Object formato = convertirFormato(lote);
        external.receiveBatch(formato);
    }
    private Object convertirFormato(LotePrendas lote){
        // montar la estructura JSON/XML que pide el proveedor externo...
        return lote;
    }
}
