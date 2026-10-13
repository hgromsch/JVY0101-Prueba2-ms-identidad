package cl.respirarysoltar.identidad.repository;

import cl.respirarysoltar.identidad.domain.ContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergencia, UUID> {

    List<ContactoEmergencia> findByUsuarioId(UUID usuarioId);
}
