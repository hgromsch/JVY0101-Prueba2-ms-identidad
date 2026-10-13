package cl.respirarysoltar.identidad.api.dto;

import cl.respirarysoltar.identidad.domain.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos de entrada para registrar o actualizar el perfil de un usuario. */
public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120)
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 160)
        String email,

        @NotNull(message = "El rol es obligatorio (PACIENTE, PSICOLOGO o ADMIN)")
        RolUsuario rol
) {
}
