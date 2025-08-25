package UI;

import entidad.Jugador;
import UI.Jugador.JugadorForm;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class PrincipalForm extends JFrame {

    private Jugador jugadorActual; // jugador seleccionado
    private final JButton btnPartidas;
    private final JButton btnJugar;
    private final JLabel lblJugador;
    private LocalDateTime fechaRegistro;

    public PrincipalForm() {
        setTitle("Menú Principal - Memoria Visual Numérica");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JButton btnJugadores = new JButton("Gestionar Jugadores");
        btnPartidas = new JButton("Gestionar Partidas");
        btnJugar = new JButton("Jugar");
        JButton btnSalir = new JButton("Salir");

        lblJugador = new JLabel("Jugador activo: ninguno");
        lblJugador.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(btnJugadores);
        panel.add(btnPartidas);
        panel.add(btnJugar);
        panel.add(btnSalir);
        panel.add(lblJugador);

        add(panel);

        // Inicialmente no hay jugador seleccionado
        btnPartidas.setEnabled(false);
        btnJugar.setEnabled(false);

        // Abrir JugadorForm
        btnJugadores.addActionListener(e -> abrirJugadorForm());

        // Abrir PartidaForm
        btnPartidas.addActionListener(e -> {
            if (jugadorActual == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un jugador primero");
                return;
            }
            PartidaForm partidaForm = new PartidaForm(this, jugadorActual);
            partidaForm.setVisible(true);
        });

        // Abrir juego
        btnJugar.addActionListener(e -> {
            if (jugadorActual == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar un jugador primero");
                return;
            }
            FrmJuego juegoForm = new FrmJuego();
            juegoForm.setJugadorActual(jugadorActual);
            juegoForm.setVisible(true);
        });

        // Salir con confirmación
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres salir?", "Salir",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
        if (jugador != null) {
            lblJugador.setText("Jugador activo: " + jugador.getNombre()
                    + " | Puntaje: " + (int)jugador.getPuntaje()); // Mostrar puntaje entero
            btnPartidas.setEnabled(true);
            btnJugar.setEnabled(true);
        } else {
            lblJugador.setText("Jugador activo: ninguno");
            btnPartidas.setEnabled(false);
            btnJugar.setEnabled(false);
        }
    }

    private void abrirJugadorForm() {
        fechaRegistro = LocalDateTime.now();

        JugadorForm jugadorForm = new JugadorForm(this);
        jugadorForm.setVisible(true);

        Jugador jugadorSelec = jugadorForm.getJugadorSeleccionado();
        if (jugadorSelec != null) {
            setJugadorActual(jugadorSelec);
            JOptionPane.showMessageDialog(this,
                    "Jugador seleccionado: " + jugadorSelec.getNombre()
                            + "\nPuntaje: " + (int)jugadorSelec.getPuntaje()
                            + "\nInicio de sesión: " + jugadorSelec.getInicioSesion());
        }
    }
}


