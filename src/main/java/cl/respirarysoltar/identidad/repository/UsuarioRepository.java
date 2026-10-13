package cl.respirarysoltar.identidad.repository;

import cl.respirarysoltar.identidad.domain.RolUsuario;
import cl.respirarysoltar.identidad.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByRol(RolUsuario rol);
}
