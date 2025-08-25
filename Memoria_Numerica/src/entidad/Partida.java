package entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "Partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Jugador_ID", nullable = false)
    @NotNull(message = "El jugador es obligatorio") // El jugador no puede ser nulo
    private Jugador jugador;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El nivel no puede estar vacío") // No puede estar vacío
    @Size(max = 20, message = "El nivel no puede superar 20 caracteres") // Limitar tamaño
    private String nivel;

    @Column(nullable = false)
    @Min(value = 1, message = "Los intentos deben ser al menos 1") // Valor mínimo permitido
    private short intentos;

    @Column(nullable = false)
    @Min(value = 0, message = "El tiempo no puede ser negativo") // Tiempo mínimo permitido
    private float tiempo;

    @Column(length = 100)
    @Size(max = 100, message = "La descripción no puede superar 100 caracteres") // Opcional, tamaño máximo
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "La fecha es obligatoria") // Fecha no puede ser nula
    private LocalDate fecha;

    @Column(nullable = false)
    @Min(value = 0, message = "El puntaje no puede ser negativo")
    private double puntaje;


    //Constructor vacio


    public Partida() {
    }

    public Partida(Jugador jugador, String nivel, short intentos, float tiempo, LocalDate fecha) {
        this.jugador = jugador;
        this.nivel = nivel;
        this.intentos = intentos;
        this.tiempo = tiempo;
        this.fecha = fecha;
    }

    //Getters and setters de la clase Partida para permiter el acceso a los atributos


    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public short getIntentos() {
        return intentos;
    }

    public void setIntentos(short intentos) {
        this.intentos = intentos;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return "Partida { " +
                "ID = " + id +
                ", Jugador = " + jugador +
                ", nivel = " + nivel + "/" +
                ", fecha" + fecha +
                " } ";
    }
}
