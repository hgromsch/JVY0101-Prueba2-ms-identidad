package cl.respirarysoltar.identidad.service;

import cl.respirarysoltar.identidad.api.dto.ContactoEmergenciaRequest;
import cl.respirarysoltar.identidad.api.dto.UsuarioRequest;
import cl.respirarysoltar.identidad.domain.ContactoEmergencia;
import cl.respirarysoltar.identidad.domain.RolUsuario;
import cl.respirarysoltar.identidad.domain.Usuario;
import cl.respirarysoltar.identidad.exception.RecursoNoEncontradoException;
import cl.respirarysoltar.identidad.exception.ReglaNegocioException;
import cl.respirarysoltar.identidad.repository.ContactoEmergenciaRepository;
import cl.respirarysoltar.identidad.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Lógica de negocio de la identidad de usuarios (RF-01). Los controladores REST
 * delegan aquí; las credenciales reales se delegan a Cognito (fuera de alcance local).
 */
@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContactoEmergenciaRepository contactoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          ContactoEmergenciaRepository contactoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contactoRepository = contactoRepository;
    }

    public Usuario registrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el email " + request.email());
        }
        Usuario usuario = new Usuario(request.nombre(), request.email(), request.rol());
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar(RolUsuario rol) {
        return rol == null ? usuarioRepository.findAll() : usuarioRepository.findByRol(rol);
    }

    @Transactional(readOnly = true)
    public Usuario obtener(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }

    public Usuario actualizar(UUID id, UsuarioRequest request) {
        Usuario usuario = obtener(id);
        usuarioRepository.findByEmail(request.email())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new ReglaNegocioException("El email " + request.email() + " ya está en uso por otro usuario");
                });
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(UUID id) {
        Usuario usuario = obtener(id);
        usuarioRepository.delete(usuario);
    }

    public ContactoEmergencia agregarContacto(UUID usuarioId, ContactoEmergenciaRequest request) {
        Usuario usuario = obtener(usuarioId);
        ContactoEmergencia contacto = new ContactoEmergencia(request.nombre(), request.telefono(), request.relacion());
        usuario.agregarContacto(contacto);
        return contactoRepository.save(contacto);
    }

    @Transactional(readOnly = true)
    public List<ContactoEmergencia> listarContactos(UUID usuarioId) {
        obtener(usuarioId);
        return contactoRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarContacto(UUID usuarioId, UUID contactoId) {
        ContactoEmergencia contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Contacto no encontrado: " + contactoId));
        if (!contacto.getUsuario().getId().equals(usuarioId)) {
            throw new ReglaNegocioException("El contacto no pertenece al usuario indicado");
        }
        contactoRepository.delete(contacto);
    }
}
