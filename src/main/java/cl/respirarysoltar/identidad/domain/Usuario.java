package cl.respirarysoltar.identidad.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Perfil de negocio de un usuario de la plataforma (paciente, psicólogo o admin).
 * La autenticación (credenciales, MFA) se delega a AWS Cognito; esta entidad solo
 * guarda el perfil y sus datos de contacto (RF-01).
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;

    /** Subject del usuario en Cognito; se completa al registrar las credenciales en AWS. */
    private String cognitoSub;

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    /** Relación OneToMany: un usuario puede registrar varios contactos de emergencia. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactoEmergencia> contactosEmergencia = new ArrayList<>();

    protected Usuario() {
        // Constructor requerido por JPA
    }

    public Usuario(String nombre, String email, RolUsuario rol) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    @PrePersist
    void alCrear() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
    }

    public void agregarContacto(ContactoEmergencia contacto) {
        contacto.setUsuario(this);
        contactosEmergencia.add(contacto);
    }

    public void quitarContacto(ContactoEmergencia contacto) {
        contactosEmergencia.remove(contacto);
        contacto.setUsuario(null);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getCognitoSub() {
        return cognitoSub;
    }

    public void setCognitoSub(String cognitoSub) {
        this.cognitoSub = cognitoSub;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public List<ContactoEmergencia> getContactosEmergencia() {
        return contactosEmergencia;
    }
}
