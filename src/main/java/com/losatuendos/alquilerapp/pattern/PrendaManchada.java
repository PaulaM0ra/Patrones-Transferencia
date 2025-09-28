package com.losatuendos.alquilerapp.pattern;

public class PrendaManchada extends PrendaDecorator {
    public PrendaManchada(PrendaComponent p){ super(p); }
    @Override
    public void marcarComoLavada(){
        // lógica extra para prioridad de manchado
        wrappee.marcarComoLavada();
    }
}
