package dao;

import entidad.Partida;
import entidad.Jugador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class PartidaDao {

    //La capa de acceso a datos de la clase Partida esta hecha para que se pueda administar los servicios y acciones que podemos realizar
    //Dentro de la base de datos con las partidas

    // Guardar o actualizar partida
    public void guardarPartida(Partida partida) {
        //Validaciones para evitar cualquier dato erroneo del administrador
        if (partida == null) {
            throw new IllegalArgumentException("La partida no puede ser nula");
        }
        Jugador j = partida.getJugador();
        if (j == null || j.getId() == null) {
            throw new IllegalArgumentException("Debe proporcionar un jugador válido");
        }
        if (partida.getNivel() == null || partida.getNivel().trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel no puede estar vacío");
        }
        if (partida.getNivel().length() > 20) {
            throw new IllegalArgumentException("El nivel no puede superar 20 caracteres");
        }
        if (partida.getIntentos() < 1) {
            throw new IllegalArgumentException("Los intentos deben ser al menos 1");
        }
        if (partida.getTiempo() < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo");
        }
        if (partida.getDescripcion() != null && partida.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La descripción no puede superar 100 caracteres");
        }
        if (partida.getFecha() == null) {
            partida.setFecha(LocalDate.now());
        }

        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // merge permite insert/update según el estado de la entidad
            em.merge(partida);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //Metodo que recibe por parametros el ID y lo busca dentro de la base de datos
    public Partida buscarPorId(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Partida.class, id);
        } finally {
            em.close();
        }
    }

    //Metodo que busca por parametros dentro de la base de datos el ID de la partida especificamente
    public boolean existeId(Long id) {
        return buscarPorId(id) != null;
    }

    //Metodo que se encarga de eliminar la partida seleccionada
    public void borrarPartida(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Partida p = em.find(Partida.class, id);
            if (p != null) {
                em.remove(p);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //Metodo que lista todas las partidas
    public List<Partida> listar(Long jugadorId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Partida p WHERE p.jugador.id = :jid ORDER BY p.fecha DESC",
                            Partida.class)
                    .setParameter("jid", jugadorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //metodo sin filtros que busca todas las partidas
    public List<Partida> listarTodas() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Partida p ORDER BY p.fecha DESC", Partida.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //Metodo para asignar puntaje
    public void guardarPartidaConPuntaje(Partida partida, double puntajeObtenido) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Validaciones
            if (partida == null) {
                throw new IllegalArgumentException("La partida no puede ser nula");
            }

            // Establecer puntaje
            partida.setPuntaje(puntajeObtenido);

            if (partida.getFecha() == null) {
                partida.setFecha(LocalDate.now());
            }

            // Guardar partida
            em.merge(partida);

            // Calcular y actualizar puntaje total del jugador
            Double puntajeTotal = em.createQuery(
                            "SELECT COALESCE(SUM(p.puntaje), 0.0) FROM Partida p WHERE p.jugador.id = :jid",
                            Double.class).setParameter("jid", partida.getJugador().getId()).getSingleResult();

            // Actualizar puntaje total en Jugador
            em.createQuery("UPDATE Jugador j SET j.puntaje = :puntaje WHERE j.id = :jid")
                    .setParameter("puntaje", puntajeTotal)
                    .setParameter("jid", partida.getJugador().getId())
                    .executeUpdate();

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}



