package UI.Jugador;

import entidad.Jugador;
import service.JugadorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class JugadorForm extends JDialog {

    private JTextField txtNombre;
    private JTextField txtPuntaje;
    private JTextField txtFechaInicio;
    private DefaultTableModel tableModel;
    private JTable table;
    private JugadorService jugadorService;
    private Jugador jugadorSeleccionado;

    public JugadorForm(Frame owner) {
        super(owner, "Administrar Jugadores", true);
        initComponentes();
    }

    private void initComponentes() {
        this.jugadorService = new JugadorService();

        setSize(700, 450);
        setLocationRelativeTo(getOwner());

        // Formulario superior: Nombre, Puntaje y Fecha de inicio
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Puntaje ganado:"));
        txtPuntaje = new JTextField("0"); // valor inicial 0
        txtPuntaje.setEditable(false); // para que no se pueda editar la informacion
        txtPuntaje.setBackground(Color.YELLOW);
        panelFormulario.add(txtPuntaje);


      //Panel de Fecha
        panelFormulario.add(new JLabel("Fecha inicio:"));
        txtFechaInicio = new JTextField(LocalDate.now().toString());
        txtFechaInicio.setEditable(false); // No editable por el usuario
        txtFechaInicio.setBackground(Color.LIGHT_GRAY);
        panelFormulario.add(txtFechaInicio);


        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnCerrar = new JButton("Cerrar");


        //agregar la funcion(accion) al boton
        btnGuardar.addActionListener(e -> guardarJugador());
        btnSeleccionar.addActionListener(e -> seleccionarJugador());
        btnBorrar.addActionListener(e -> borrarJugador());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCerrar);

        // Tabla
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Puntaje", "Inicio Sesión"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla no editable
            }
        };
        table = new JTable(tableModel);
        cargarJugadores();

        // Layout
        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarJugador() {
        String nombre = txtNombre.getText().trim();
        String puntajeStr = txtPuntaje.getText().trim();
        String fechaStr = txtFechaInicio.getText().trim();

        if (nombre.isEmpty() || puntajeStr.isEmpty() || fechaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        double puntaje = 0;
        LocalDate inicioSesion = LocalDate.now();



        try {
            inicioSesion = LocalDate.parse(fechaStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Se asigna la fecha actual.");
            inicioSesion = LocalDate.now();
        }

        try {
            // Crear el jugador y setear todos los datos
            Jugador jugador = new Jugador();
            jugador.setNombre(nombre);
            jugador.setPuntaje(puntaje);
            jugador.setInicioSesion(inicioSesion);

            // Guardar usando el servicio
            jugadorService.registrarJugador(
                    jugador.getNombre(),
                    jugador.getPuntaje(),
                    jugador.getInicioSesion(),
                    inicioSesion != null ? inicioSesion.atStartOfDay() : LocalDateTime.now()
            ); // usa solo nombre según tu service
            JOptionPane.showMessageDialog(this, "Jugador guardado exitosamente");
            cargarJugadores();

            txtNombre.setText("");
            txtPuntaje.setText("0");
            txtFechaInicio.setText(LocalDate.now().toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar jugador: " + ex.getMessage());
        }
    }

    private void cargarJugadores() {
        tableModel.setRowCount(0);
        List<Jugador> jugadores = jugadorService.listar();
        for (Jugador j : jugadores) {
            tableModel.addRow(new Object[]{j.getId(), j.getNombre(), j.getPuntaje(), j.getInicioSesion()});
        }
    }

    private void seleccionarJugador() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            Long id = (Long) tableModel.getValueAt(fila, 0);
            String nombre = (String) tableModel.getValueAt(fila, 1);
            double puntaje = (double) tableModel.getValueAt(fila, 2);
            LocalDate inicioSesion = (LocalDate) tableModel.getValueAt(fila, 3);

            jugadorSeleccionado = new Jugador();
            jugadorSeleccionado.setId(id);
            jugadorSeleccionado.setNombre(nombre);
            jugadorSeleccionado.setPuntaje(puntaje);
            jugadorSeleccionado.setInicioSesion(inicioSesion);

            JOptionPane.showMessageDialog(this, "Jugador seleccionado: " + nombre);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador de la tabla.");
        }
    }

    public Jugador getJugadorSeleccionado() {
        return jugadorSeleccionado;
    }

    private void borrarJugador() {
        int fila = table.getSelectedRow();
        if (fila != -1) {
            Long id = (Long) tableModel.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas borrar este jugador?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    jugadorService.borrarJugador(id);
                    JOptionPane.showMessageDialog(this, "Jugador borrado exitosamente");
                    cargarJugadores();
                    if (jugadorSeleccionado != null && id.equals(jugadorSeleccionado.getId())) {
                        jugadorSeleccionado = null;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al borrar: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador de la tabla.");
        }
    }
}




