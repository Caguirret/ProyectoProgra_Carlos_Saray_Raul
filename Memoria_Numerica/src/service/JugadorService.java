package service;

import dao.JugadorDao;
import entidad.Jugador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JugadorService {

    //Dentro de la clase service se define los metodos del dao y le damos una inicializacion llamandolos desde el service
    //Aqui es donde se encuentra los metodos que podemos realizar con una Jugador o cualquier Objeto

    private final JugadorDao jugadorDao;

    public JugadorService() {
        this.jugadorDao = new JugadorDao();
    }

    // Crear un nuevo jugador (con todos los datos)
    public Jugador registrarJugador(String nombre, double puntaje, LocalDate inicioSesion, LocalDateTime fechaRegistro) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }

        Jugador existe = jugadorDao.buscarNombre(nombre.trim());
        if (existe != null) {
            throw new Exception("Ese jugador ya existe");
        }

        Jugador nuevo = new Jugador();
        nuevo.setNombre(nombre.trim());
        nuevo.setPuntaje(puntaje);
        nuevo.setInicioSesion(inicioSesion != null ? inicioSesion : LocalDate.now());
        nuevo.setFechaRegistro(fechaRegistro != null ? fechaRegistro : LocalDateTime.now());

        jugadorDao.registrarJugador(nuevo);
        return nuevo;
    }

    // Obtener un jugador por ID
    public Jugador buscarPorId(Long id) {
        if (id == null) return null;
        return jugadorDao.buscarPorId(id);
    }

    // Obtener un jugador por nombre
    public Jugador buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return null;
        return jugadorDao.buscarNombre(nombre.trim());
    }

    // Listar todos los jugadores
    public List<Jugador> listar() {
        return jugadorDao.listarJugador();
    }

    // Actualizar un jugador existente
    public void actualizarJugador(Jugador jugador) throws Exception {
        if (jugador == null || jugador.getId() == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo y debe tener un ID válido");
        }
        Jugador existente = jugadorDao.buscarPorId(jugador.getId());
        if (existente == null) {
            throw new Exception("El jugador no existe en la base de datos");
        }

        // Actualizar todos los campos
        existente.setNombre(jugador.getNombre());
        existente.setPuntaje(jugador.getPuntaje());
        existente.setInicioSesion(jugador.getInicioSesion() != null ? jugador.getInicioSesion() : LocalDate.now());
        existente.setFechaRegistro(jugador.getFechaRegistro() != null ? jugador.getFechaRegistro() : LocalDateTime.now());

        jugadorDao.guardar(existente);
    }

    // Eliminar jugador por ID
    public void borrarJugador(Long id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID del jugador no puede ser nulo");
        }
        Jugador existente = jugadorDao.buscarPorId(id);
        if (existente == null) {
            throw new Exception("El jugador con ID " + id + " no existe");
        }
        jugadorDao.borrarJugador(id);
    }
}





