package dao;

import entidad.Jugador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JugadorDao {

    //La capa de acceso a datos de la clase jugador esta hecha para que se pueda administar los servicios y acciones que podemos realizar
    //Dentro de la base de datos

    // Crear / Registrar jugador
    public void registrarJugador(Jugador jugador) {
        if (jugador == null) throw new IllegalArgumentException("El jugador no puede ser nulo");

        // Inicializar valores por defecto si es necesario
        if (jugador.getInicioSesion() == null) jugador.setInicioSesion(LocalDate.now());
        if (jugador.getFechaRegistro() == null) jugador.setFechaRegistro(LocalDateTime.now());
        if (jugador.getPuntaje() == 0) jugador.setPuntaje(0);

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(jugador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Leer: buscar por nombre, lista a todos los jugadores con ese nombre
    public Jugador buscarNombre(String nombre) {
        //Validacion para no realizar una busqueda si es nulo
        if (nombre == null || nombre.trim().isEmpty()) return null;

        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT j FROM Jugador j WHERE j.nombre = :nombre", Jugador.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Leer: buscar por ID, Busca y devuelve al unico jugador con la ID en especifico
    public Jugador buscarPorId(Long id) {
        if (id == null) return null;

        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    // Verificar existencia por ID
    public boolean existePorId(Long id) {
        return buscarPorId(id) != null;
    }

    // Listar todos los jugadores
    public List<Jugador> listarJugador() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT j FROM Jugador j", Jugador.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Actualizar jugador
    public void guardar(Jugador jugador) {
        //Validaciones para evitar errores de los usuarios al intentar guardar un jugador
        if (jugador == null) throw new IllegalArgumentException("El jugador no puede ser nulo");

        if (jugador.getInicioSesion() == null) jugador.setInicioSesion(LocalDate.now());
        if (jugador.getFechaRegistro() == null) jugador.setFechaRegistro(LocalDateTime.now());
        if (jugador.getPuntaje() == 0) jugador.setPuntaje(0);

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(jugador);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Eliminar jugador
    public void borrarJugador(Long id) {
        //Este metodo busca al jugador con su respectivo ID, lo recibe por parametros y lo elimina
        if (id == null) throw new IllegalArgumentException("El ID del jugador no puede ser nulo");

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Jugador jugador = em.find(Jugador.class, id);
            if (jugador != null) {
                em.remove(jugador);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }

    }
}

