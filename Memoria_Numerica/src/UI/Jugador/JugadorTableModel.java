package UI.Jugador;

import entidad.Jugador;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class JugadorTableModel extends AbstractTableModel {
    //Columnas definidas para almacenar los atributos de un jugador
    private String[] columnas = {"ID", "Nombre", "Puntaje","registro", "Inicio de sesion al juego"};

    //Defininimos una lista para que regrese los datos o en si todos los jugadores
    private List<Jugador> jugadores;

    public JugadorTableModel(List<Jugador> lista) {
        this.jugadores = lista;
    }

    public void setJugadores(List<Jugador> jugadores){
        this.jugadores = jugadores;
        fireTableDataChanged();
    }

    public Jugador getJugadores(int row) {
        return jugadores.get(row);
    }

    @Override
    public int getRowCount() {
        return jugadores.size();
    }

    @Override
    public int getColumnCount() {
        return jugadores.toArray().length;
    }

    @Override
    public Object getValueAt(int fil, int colum) {
        Jugador jugador = jugadores.get(fil);
        return switch (colum){
            case 0 -> jugador.getId();
            case 1 -> jugador.getNombre();
            case 2 -> jugador.getPuntaje();
            case 3 -> jugador.getInicioSesion();
            case 4 -> jugador.getFechaRegistro();
            default -> null;
        };
    }







}
