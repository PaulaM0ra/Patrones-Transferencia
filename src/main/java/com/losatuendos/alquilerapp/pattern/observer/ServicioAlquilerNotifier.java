package com.losatuendos.alquilerapp.pattern.observer;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServicioAlquilerNotifier {
    private final List<AlquilerEventListener> listeners = new ArrayList<>();

    public void subscribe(AlquilerEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(AlquilerEventListener listener) {
        listeners.remove(listener);
    }

    public void notify(String eventType, Object data) {
        for (AlquilerEventListener listener : listeners) {
            listener.update(eventType, data);
        }
    }
}
