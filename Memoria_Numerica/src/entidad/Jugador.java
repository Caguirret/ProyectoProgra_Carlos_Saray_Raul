package entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Jugadores")
public class Jugador {

    //Clase jugador con sus atributos y validaciones para evitar datos nulos dentro de la base de datos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Min(value = 0, message = "El puntaje no puede ser negativo")
    @Column(nullable = false)
    private double puntaje;

    @NotNull(message = "La fecha de inicio de sesión es obligatoria")
    @Column(name = "inicio_sesion", nullable = false)
    private LocalDate inicioSesion;

    @NotNull(message = "La fecha de registro es obligatoria")
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    // Constructor vacío por defecto de la clase
    public Jugador() {
        this.puntaje = 0;
        this.inicioSesion = LocalDate.now();
        this.fechaRegistro = LocalDateTime.now();
    }

    // Constructor lleno de la mayoria de los atributos
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.inicioSesion = LocalDate.now();
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters y Setters de la clase Jugador para tener acceso a ellos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public LocalDate getInicioSesion() {
        return inicioSesion;
    }

    public void setInicioSesion(LocalDate inicioSesion) {
        this.inicioSesion = inicioSesion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    //Metodo que devuelve de manera ordenada los atributos
    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", puntaje=" + puntaje +
                ", inicioSesion=" + inicioSesion +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}

