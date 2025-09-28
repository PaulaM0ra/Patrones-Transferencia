package com.losatuendos.alquilerapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "servicios_alquiler")
public class ServicioAlquiler {

    @Id
    private Long id;

    private LocalDate fechaSolic;
    private LocalDate fechaAlqui;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @ManyToMany
    @JoinTable(
            name = "servicio_prendas",
            joinColumns = @JoinColumn(name = "servicio_id"),
            inverseJoinColumns = @JoinColumn(name = "prenda_ref")
    )
    private List<Prenda> prendas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaSolic() {
        return fechaSolic;
    }

    public void setFechaSolic(LocalDate fechaSolic) {
        this.fechaSolic = fechaSolic;
    }

    public LocalDate getFechaAlqui() {
        return fechaAlqui;
    }

    public void setFechaAlqui(LocalDate fechaAlqui) {
        this.fechaAlqui = fechaAlqui;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Prenda> getPrendas() {
        return prendas;
    }

    public void setPrendas(List<Prenda> prendas) {
        this.prendas = prendas;
    }
}
