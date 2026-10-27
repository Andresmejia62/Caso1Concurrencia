package com.appregistros.caso1.controller;

import com.appregistros.caso1.model.Reservas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import java.util.concurrent.ThreadLocalRandom;

public class ReservasController {
    private static final int SOLICITUDES = 10;

    @FXML private ProgressBar pbAgente1;
    @FXML private ProgressBar pbAgente2;
    @FXML private ProgressBar pbAgente3;

    @FXML private Label lblAgente1;
    @FXML private Label lblAgente2;
    @FXML private Label lblAgente3;
    @FXML private Label lblDisponibles;
    @FXML private Label lblConfirmadas;
    @FXML private Label lblRechazadas;
    @FXML private TextArea txtRegistro;
    @FXML private Button btnIniciar;
    @FXML private Button btnLimpiar;

    private final Reservas reservas = new Reservas();
    private boolean enEjecucion = false;   // solo se usa en el hilo de JavaFX

    @FXML
    private void initialize() {
        reiniciarInterfaz();
    }

    @FXML
    private void iniciarReservas() {
        if (enEjecucion) {                 // evita inicios duplicados
            return;
        }
        enEjecucion = true;
        reservas.reiniciar();              // nueva simulación = datos restablecidos
        reiniciarInterfaz();
        btnIniciar.setDisable(true);
        btnLimpiar.setDisable(true);

        Thread agente1 = new Thread(() -> procesarReservas("Agente 1", pbAgente1, lblAgente1));
        Thread agente2 = new Thread(() -> procesarReservas("Agente 2", pbAgente2, lblAgente2));
        Thread agente3 = new Thread(() -> procesarReservas("Agente 3", pbAgente3, lblAgente3));

        agente1.start();
        agente2.start();
        agente3.start();

        Thread supervisor = new Thread(() -> {
            try {
                agente1.join();
                agente2.join();
                agente3.join();
                Platform.runLater(() -> {
                    actualizarTotales();   // valores finales, ya consistentes
                    txtRegistro.appendText("------------Procesamiento finalizado----------------\n");
                    enEjecucion = false;
                    btnIniciar.setDisable(false);
                    btnLimpiar.setDisable(false);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        supervisor.start();
    }

    private void procesarReservas(String agente, ProgressBar pbAgente, Label lblAgente) {
        int ok = 0;
        int no = 0;
        for (int i = 1; i <= SOLICITUDES; i++) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 900));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            boolean confirmada = reservas.reservar();   // comprobar + descontar atómico
            if (confirmada) {
                ok++;
            } else {
                no++;
            }

            int solicitudActual = i;
            int confirmadasAgente = ok;
            int rechazadasAgente = no;
            double progreso = i / (double) SOLICITUDES;

            Platform.runLater(() -> {
                pbAgente.setProgress(progreso);
                lblAgente.setText(agente + ": " + confirmadasAgente + " confirmadas, "
                        + rechazadasAgente + " rechazadas");
                txtRegistro.appendText(agente + " solicitud " + solicitudActual + " -> "
                        + (confirmada ? "CONFIRMADA" : "RECHAZADA") + "\n");
                actualizarTotales();
            });
        }
    }
    private void actualizarTotales() {
        lblDisponibles.setText(String.valueOf(reservas.getDisponibles()));
        lblConfirmadas.setText(String.valueOf(reservas.getConfirmadas()));
        lblRechazadas.setText(String.valueOf(reservas.getRechazadas()));
    }


}