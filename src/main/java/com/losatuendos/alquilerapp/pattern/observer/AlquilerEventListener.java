package com.losatuendos.alquilerapp.pattern.observer;

public interface AlquilerEventListener {
    void update(String eventType, Object data);
}
