package com.losatuendos.alquilerapp.id;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ServicioAlquilerIDGenerator {

    private final AtomicLong counter = new AtomicLong(1);

    public long getNextId() {
        return counter.getAndIncrement();
    }
}
