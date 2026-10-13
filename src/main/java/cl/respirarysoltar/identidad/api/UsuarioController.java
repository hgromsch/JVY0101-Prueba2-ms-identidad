package cl.respirarysoltar.identidad.api;

import cl.respirarysoltar.identidad.api.dto.ContactoEmergenciaRequest;
import cl.respirarysoltar.identidad.api.dto.UsuarioRequest;
import cl.respirarysoltar.identidad.domain.ContactoEmergencia;
import cl.respirarysoltar.identidad.domain.RolUsuario;
import cl.respirarysoltar.identidad.domain.Usuario;
import cl.respirarysoltar.identidad.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * API REST de identidad y perfiles de usuario (RF-01).
 * En AWS las credenciales/MFA se delegan a Cognito; este servicio persiste el perfil de negocio.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioRequest request) {
        Usuario creado = service.registrar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getId()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @GetMapping
    public List<Usuario> listar(@RequestParam(required = false) RolUsuario rol) {
        return service.listar(rol);
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable UUID id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contactos")
    public ResponseEntity<ContactoEmergencia> agregarContacto(@PathVariable UUID id,
                                                              @Valid @RequestBody ContactoEmergenciaRequest request) {
        ContactoEmergencia creado = service.agregarContacto(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}/contactos")
    public List<ContactoEmergencia> listarContactos(@PathVariable UUID id) {
        return service.listarContactos(id);
    }

    @DeleteMapping("/{id}/contactos/{contactoId}")
    public ResponseEntity<Void> eliminarContacto(@PathVariable UUID id, @PathVariable UUID contactoId) {
        service.eliminarContacto(id, contactoId);
        return ResponseEntity.noContent().build();
    }
}
