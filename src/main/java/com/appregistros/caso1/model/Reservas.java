package com.appregistros.caso1.model;

import javafx.fxml.FXML;

public class Reservas {
    public static final int TOTAL_ENTRADAS = 20;

    private int cantidadReservas = 0;
    private int totalRecaudado = 0;
    private int disponibles = TOTAL_ENTRADAS;
    private int confirmadas = 0;
    private int rechazadas = 0;

    public synchronized void registrarReserva(int monto) {
        cantidadReservas++;
        totalRecaudado += monto;
        if (disponibles > 0) {
            disponibles--;
            confirmadas++;
        } else {
            rechazadas++;
        }
    }

    public synchronized int getCantidadReservas() {
        return cantidadReservas;
    }

    public synchronized int getTotalRecaudado() {
        return totalRecaudado;
    }

    public synchronized void reiniciar() {
        cantidadReservas = 0;
        totalRecaudado = 0;
        disponibles = TOTAL_ENTRADAS;
        confirmadas = 0;
        rechazadas = 0;
    }

    public synchronized boolean reservar() {
        if (disponibles > 0) {
            disponibles--;
            confirmadas++;
            cantidadReservas++;
            return true;
        }
        rechazadas++;
        return false;
    }

    public synchronized int getDisponibles() {
        return disponibles;
    }

    public synchronized int getConfirmadas() {
        return confirmadas;
    }

    public synchronized int getRechazadas() {
        return rechazadas;
    }

    @FXML
    public synchronized void iniciarReserva() {
        reiniciar();
    }

    @FXML
    public synchronized void IniciarReserva() {
        iniciarReserva();
    }
}
