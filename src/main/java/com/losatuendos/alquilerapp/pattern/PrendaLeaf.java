package com.losatuendos.alquilerapp.pattern;

import com.losatuendos.alquilerapp.model.Prenda;

public class PrendaLeaf implements PrendaComponent {
    private final Prenda prenda;
    public PrendaLeaf(Prenda p){
        this.prenda = p;
    }
    @Override
    public double calcularValor(){
        return prenda.getValorAlquiler();
    }
    @Override
    public void marcarComoLavada(){ /* marcar flag o fecha */ }

    /** Para que la fachada pueda extraer la entidad Prenda */
    public Prenda getPrenda() {
        return this.prenda;
    }
}
