package cl.respirarysoltar.identidad.service;

import cl.respirarysoltar.identidad.api.dto.UsuarioRequest;
import cl.respirarysoltar.identidad.domain.RolUsuario;
import cl.respirarysoltar.identidad.domain.Usuario;
import cl.respirarysoltar.identidad.exception.RecursoNoEncontradoException;
import cl.respirarysoltar.identidad.exception.ReglaNegocioException;
import cl.respirarysoltar.identidad.repository.ContactoEmergenciaRepository;
import cl.respirarysoltar.identidad.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContactoEmergenciaRepository contactoRepository;

    @InjectMocks
    private UsuarioService service;

    private UsuarioRequest requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new UsuarioRequest("Ana Torres", "ana@correo.cl", RolUsuario.PACIENTE);
    }

    @Test
    void registrarConEmailNuevoGuardaElUsuario() {
        when(usuarioRepository.existsByEmail("ana@correo.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario creado = service.registrar(requestValido);

        assertNotNull(creado);
        assertEquals("Ana Torres", creado.getNombre());
        assertEquals(RolUsuario.PACIENTE, creado.getRol());
    }

    @Test
    void registrarConEmailDuplicadoLanzaConflicto() {
        when(usuarioRepository.existsByEmail("ana@correo.cl")).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> service.registrar(requestValido));
    }

    @Test
    void obtenerConIdInexistenteLanzaNoEncontrado() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtener(id));
    }
}
