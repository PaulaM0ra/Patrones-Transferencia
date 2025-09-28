package com.losatuendos.alquilerapp.pattern;

import java.util.ArrayList;
import java.util.List;

public class LotePrendas implements PrendaComponent {
    private final List<PrendaComponent> componentes = new ArrayList<>();
    public void add(PrendaComponent c){ componentes.add(c); }
    public void remove(PrendaComponent c){ componentes.remove(c); }

    @Override
    public double calcularValor(){
        return componentes.stream().mapToDouble(PrendaComponent::calcularValor).sum();
    }
    @Override
    public void marcarComoLavada(){
        componentes.forEach(PrendaComponent::marcarComoLavada);
    }
}
