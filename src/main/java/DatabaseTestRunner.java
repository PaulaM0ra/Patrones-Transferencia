package com.losatuendos.alquilerapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseTestRunner implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseTestRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Conexión a la base de datos establecida: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
        }
    }
}
