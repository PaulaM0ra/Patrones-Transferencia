package com.losatuendos.alquilerapp.pattern;

public abstract class PrendaDecorator implements PrendaComponent {
    protected final PrendaComponent wrappee;
    protected PrendaDecorator(PrendaComponent p){
        this.wrappee = p;
    }
    @Override public double calcularValor(){
        return wrappee.calcularValor();
    }
    @Override public void marcarComoLavada(){
        wrappee.marcarComoLavada();
    }

    /** Permite acceder desde fuera al componente envuelto */
    public PrendaComponent getWrappee() {
        return this.wrappee;
    }
}
