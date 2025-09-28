package com.losatuendos.alquilerapp.pattern.observer;

import org.springframework.stereotype.Component;

@Component
public class AdministradorLoggerObserver implements AlquilerEventListener {
    @Override
    public void update(String eventType, Object data) {
        System.out.println("Observer: Evento '" + eventType + "' recibido con datos: " + data.toString());
    }
}
