package UI;

import entidad.Jugador;
import entidad.Partida;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PartidaTableModel extends AbstractTableModel {
    //Columnas definidas para almacenar los atributos de un jugador
    private String[] columnas = {"", "", "", ""};

    //Defininimos una lista para que regrese los datos o en si todos los jugadores
    private List<Partida> partidas;

    public PartidaTableModel(List<Partida> lista) {
        this.partidas = lista;
    }

    public void setJugadores(List<Partida> partidas){
        this.partidas = partidas;
        fireTableDataChanged();
    }

    public Partida getPartidas(int row) {
        return partidas.get(row);
    }

    @Override
    public int getRowCount() {
        return partidas.size();
    }

    @Override
    public int getColumnCount() {
        return partidas.toArray().length;
    }

    @Override
    public Object getValueAt(int fil, int colum) {
        Partida partida = partidas.get(fil);
        return switch (colum){
            case 0 -> partida.getId();
            case 1 -> partida.getJugador();
            case 2 -> partida.getPuntaje();
            case 3 -> partida.getNivel();
            case 4 -> partida.getDescripcion();
            case 5 -> partida.getTiempo();
            case 6 -> partida.getFecha();
            case 7 -> partida.getIntentos();
            default -> null;
        };
    }
}
