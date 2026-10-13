package cl.respirarysoltar.identidad.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Contacto de emergencia de un usuario (lado "muchos" de la relación ManyToOne).
 * Se usa principalmente para pacientes, ante cualquier eventualidad en sesión.
 */
@Entity
@Table(name = "contactos_emergencia")
public class ContactoEmergencia {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String telefono;

    /** Relación con el contacto: "madre", "pareja", etc. */
    @Column(nullable = false, length = 60)
    private String relacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    protected ContactoEmergencia() {
        // Constructor requerido por JPA
    }

    public ContactoEmergencia(String nombre, String telefono, String relacion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.relacion = relacion;
    }

    public UUID getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
