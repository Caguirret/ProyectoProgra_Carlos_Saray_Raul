package UI;

import entidad.Jugador;
import entidad.Partida;
import service.PartidaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PartidaForm extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private PartidaService partidaService;
    private Jugador jugadorActual;

    public PartidaForm(Frame owner, Jugador jugador) {
        super(owner, "Gestionar Partidas", true);
        this.jugadorActual = jugador;
        this.partidaService = new PartidaService();

        setSize(900, 500); // Ancho aumentado para la columna puntaje
        setLocationRelativeTo(owner);

        // Botones
        JButton btnBorrar = new JButton("Borrar Partida");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnCerrar = new JButton("Cerrar");


        btnBorrar.addActionListener(e -> borrarPartida());
        btnRefrescar.addActionListener(e -> cargarPartidas());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnBorrar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnCerrar);

        // Tabla MODIFICADA para incluir puntaje
        tableModel = new DefaultTableModel(
                new String[]{"ID","Jugador", "Nivel", "Intentos", "Tiempo(s)", "Puntaje", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        if (jugadorActual == null) {
            btnBorrar.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "No se recibió un jugador. Solo se pueden ver partidas.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

        cargarPartidas();
    }

    // Metodo para agregar partida
    private void borrarPartida() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una partida en la tabla para borrar.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas borrar la partida seleccionada?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            partidaService.borrarPartida(id);
            JOptionPane.showMessageDialog(this, "Partida borrada correctamente.");
            cargarPartidas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar partida: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mmetodo para cargar partida
    private void cargarPartidas() {
        tableModel.setRowCount(0);
        try {
            List<Partida> partidas = jugadorActual != null
                    ? partidaService.listarPartidaServ(jugadorActual.getId())
                    : partidaService.listar();

            for (Partida p : partidas) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getJugador() != null ? p.getJugador().getNombre() : "N/A",
                        p.getNivel(),
                        p.getIntentos(),
                        p.getTiempo(),
                        (int)p.getPuntaje(), // COLUMNA PUNTAJE AGREGADA
                        p.getFecha(),
                        p.getDescripcion() != null ? p.getDescripcion() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar partidas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
