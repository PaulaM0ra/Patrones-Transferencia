package com.losatuendos.alquilerapp.pattern;

public class PrendaPrioritaria extends PrendaDecorator {
    public PrendaPrioritaria(PrendaComponent p){ super(p); }
    @Override
    public void marcarComoLavada(){
        // lógica para envío urgente
        wrappee.marcarComoLavada();
    }
}
