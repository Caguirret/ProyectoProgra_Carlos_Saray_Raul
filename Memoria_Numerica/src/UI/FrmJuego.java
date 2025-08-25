
package UI;

import entidad.Jugador;
import entidad.Partida;
import service.PartidaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FrmJuego extends JFrame {

    private JPanel panelTablero;
    private JButton[][] botones;
    private int[][] numeros;
    private JButton primerBoton = null;
    private int primerNum = -1;
    private int intentos = 0;

    private JLabel labelinten;
    private Timer timer;
    private int tiempoRest;
    private JLabel labeltiem;
    private Jugador jugadorActual;

    // VARIABLES PARA MEJORAS
    private int parejasEncontradas = 0;
    private int totalParejas = 0;
    private int botonesSeleccionados = 0; // Controla máximo 2 selecciones
    private float tiempoLimiteOriginal;
    private String nivelSeleccionado;

    private PartidaService partidaService = new PartidaService();

    public FrmJuego() {
        setTitle("Juego de Memoria Numérica Visual");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior (info)
        JPanel panelInfo = new JPanel(new GridLayout(1, 2));
        labelinten = new JLabel("Intentos: 0");
        labeltiem = new JLabel("Tiempo: 0s");
        panelInfo.add(labelinten);
        panelInfo.add(labeltiem);
        add(panelInfo, BorderLayout.NORTH);

        // Panel central (tablero)
        panelTablero = new JPanel();
        add(panelTablero, BorderLayout.CENTER);

        // Seleccionar nivel
        String[] niveles = {"Noob 2x2", "Mas o Menos 4x4", "Nivel Dios 5x5"};
        String nivel = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona nivel:",
                "Nivel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                niveles,
                niveles[0]
        );

        int fil = 2, column = 2;
        int tiempoLimit = 60;

        if (nivel != null) {
            nivelSeleccionado = nivel;
            switch (nivel) {
                case "Noob 2x2":
                    fil = 2; column = 2; tiempoLimit = 60; totalParejas = 2; break;
                case "Mas o Menos 4x4":
                    fil = 4; column = 4; tiempoLimit = 180; totalParejas = 8; break;
                case "Nivel Dios 5x5":
                    fil = 5; column = 5; tiempoLimit = 240; totalParejas = 12; break;
                default:
                    fil = 2; column = 2; tiempoLimit = 60; totalParejas = 2; break;
            }
        } else {
            nivelSeleccionado = "Noob 2x2";
            totalParejas = 2;
        }

        tiempoRest = tiempoLimit;
        tiempoLimiteOriginal = tiempoLimit;
        labeltiem.setText("Tiempo: " + tiempoRest + "s");


        numeros = generarMatrizNumeros(fil, column);
        botones = new JButton[fil][column];

        panelTablero.setLayout(new GridLayout(fil, column, 5, 5));
        for (int i = 0; i < fil; i++) {
            for (int j = 0; j < column; j++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 24));
                int valor = numeros[i][j];

                button.addActionListener((ActionEvent e) -> manejarBoton(button, valor));

                botones[i][j] = button;
                panelTablero.add(button);
            }
        }

        // Temporizador principal
        timer = new Timer(1000, e -> {
            tiempoRest--;
            labeltiem.setText("Tiempo: " + tiempoRest + "s");
            if (tiempoRest <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "¡Se acabó el tiempo! Intentos: " + intentos);
                dispose();
            }
        });

        timer.setInitialDelay(1000);
        timer.start();
    }

    // METODO LOCAL MEJORADO (renombrado para claridad)
    private int[][] generarMatrizNumeros(int fil, int column) {
        int total = fil * column;
        List<Integer> lista = new ArrayList<>();
        int pares = totalParejas; // Usar la variable ya calculada

        // Pares desde 1..pares
        for (int i = 1; i <= pares; i++) {
            lista.add(i);
            lista.add(i);
        }

        // Si hay celda impar, agregar valores únicos adicionales
        int next = pares + 1;
        while (lista.size() < total) {
            lista.add(next++);
        }

        Collections.shuffle(lista);
        int[][] matriz = new int[fil][column];
        Iterator<Integer> it = lista.iterator();
        for (int i = 0; i < fil; i++) {
            for (int j = 0; j < column; j++) {
                if (it.hasNext()) matriz[i][j] = it.next();
            }
        }
        return matriz;
    }

    // METODO MEJORADO PARA MANEJAR BOTONES
    private void manejarBoton(JButton button, int valor) {
        // VALIDAR: máximo 2 botones seleccionados a la vez
        if (botonesSeleccionados >= 2) {
            return;
        }

        // Evitar doble clic en el mismo botón
        if (primerBoton == button) {
            return;
        }

        button.setText(String.valueOf(valor));
        button.setEnabled(false);
        botonesSeleccionados++;

        if (primerBoton == null) {
            // Primera selección
            primerBoton = button;
            primerNum = valor;
        } else {
            // Segunda selección
            intentos++;
            labelinten.setText("Intentos: " + intentos);

            if (valor == primerNum) {
                // ACIERTO
                parejasEncontradas++;
                primerBoton = null;
                primerNum = -1;
                botonesSeleccionados = 0;

                if (parejasEncontradas >= totalParejas) {
                    timer.stop();
                    float tiempoUsado = tiempoLimiteOriginal - tiempoRest;

                    // CALCULAR Y MOSTRAR PUNTAJE
                    double puntajeObtenido = partidaService.calcularPuntaje(
                            nivelSeleccionado, intentos, tiempoUsado, tiempoLimiteOriginal);

                    JOptionPane.showMessageDialog(this,
                            "¡GANASTE!\n" +
                                    "Intentos: " + intentos + "\n" +
                                    "Tiempo usado: " + (int)tiempoUsado + "s\n" +
                                    "Puntaje obtenido: " + (int)puntajeObtenido + " puntos!");

                    guardarPartidaConPuntaje(tiempoUsado, puntajeObtenido);
                    dispose();
                }
            } else {
                // ERROR: ocultar después de 1 segundo
                Timer t = new Timer(1000, ev -> {
                    button.setText("");
                    button.setEnabled(true);
                    if (primerBoton != null) {
                        primerBoton.setText("");
                        primerBoton.setEnabled(true);
                    }
                    primerBoton = null;
                    primerNum = -1;
                    botonesSeleccionados = 0; // CRÍTICO: resetear contador
                });
                t.setRepeats(false);
                t.start();
            }
        }
    }

    private void guardarPartidaConPuntaje(float tiempoUsado, double puntaje) {
        if (jugadorActual == null) {
            JOptionPane.showMessageDialog(this, "No hay jugador asignado. No se guardará la partida.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Partida partida = new Partida();
            partida.setJugador(jugadorActual);
            partida.setNivel(nivelSeleccionado);
            partida.setIntentos((short) intentos);
            partida.setTiempo(tiempoUsado);
            partida.setFecha(LocalDate.now());
            partida.setPuntaje(puntaje);

            partidaService.finalizarPartida(partida, nivelSeleccionado, intentos,
                    tiempoUsado, tiempoLimiteOriginal);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar partida: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }
}