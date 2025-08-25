
package service;

import dao.PartidaDao;
import entidad.Jugador;
import entidad.Partida;

import java.time.LocalDate;
import java.util.List;

public class PartidaService {

    //Dentro de la clase service se define los metodos del dao y le damos una inicializacion llamandolos desde el service
    //Aqui es donde se encuentra los metodos que podemos realizar con una Partida o cualquier Objeto
    private final PartidaDao partidaDao;

    public PartidaService() {
        this.partidaDao = new PartidaDao();
    }

    // MANTENER MÉTODOS DE CONSULTA - ESTOS SÍ PERTENECEN AQUÍ
    public List<Partida> listarPartidaServ(Long jugadorId){
        if (jugadorId == null){
            throw new IllegalArgumentException("El id del jugador es obligatorio");
        }
        return partidaDao.listar(jugadorId);
    }

    //este metodom reemplaza a HistorialService.obtenerHistorial()
    public List<Partida> obtenerHistorial(Long jugadorId) {
        return listarPartidaServ(jugadorId); // Reutilizar metodo existente
    }

    public List<Partida> listar(){
        return partidaDao.listarTodas();
    }

    public Partida buscarPorId(Long id){
        if (id == null) return null;
        return partidaDao.buscarPorId(id);
    }

    public void borrarPartida(Long id){
        if (id == null) throw new IllegalArgumentException("El id no puede ser nulo");
        if (!partidaDao.existeId(id)) throw new IllegalArgumentException("La partida no existe");
        partidaDao.borrarPartida(id);
    }

    // MÉTODOS DE PUNTAJE - ESTOS SÍ PERTENECEN AQUÍ
    public double calcularPuntaje(String nivel, int intentos, float tiempoUsado, float tiempoLimite) {
        double puntajeBase = 0;

        // Puntaje base según nivel
        switch (nivel) {
            case "Noob 2x2":
                puntajeBase = 100;
                break;
            case "Mas o Menos 4x4":
                puntajeBase = 300;
                break;
            case "Nivel Dios 5x5":
                puntajeBase = 500;
                break;
            default:
                puntajeBase = 50;
                break;
        }

        // Bonus por numero de intentos (menos intentos = más puntos)
        int paresEsperados = nivel.equals("Noob 2x2") ? 2 :
                nivel.equals("Mas o Menos 4x4") ? 8 : 12;
        double bonusIntentos = Math.max(0, (paresEsperados * 2 - intentos) * 10);

        // Bonus por tiempo restante
        float tiempoRestante = tiempoLimite - tiempoUsado;
        double bonusTiempo = Math.max(0, tiempoRestante * 2);

        return puntajeBase + bonusIntentos + bonusTiempo;
    }

    // Finaliza partida calculando puntaje y guardando
    public void finalizarPartida(Partida partida, String nivel, int intentos,
                                 float tiempoUsado, float tiempoLimite) {
        double puntajeObtenido = calcularPuntaje(nivel, intentos, tiempoUsado, tiempoLimite);
        partidaDao.guardarPartidaConPuntaje(partida, puntajeObtenido);
    }
}