package cl.respirarysoltar.identidad.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos de entrada para registrar un contacto de emergencia de un usuario. */
public record ContactoEmergenciaRequest(
        @NotBlank(message = "El nombre del contacto es obligatorio")
        @Size(max = 120)
        String nombre,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 30)
        String telefono,

        @NotBlank(message = "La relación con el usuario es obligatoria")
        @Size(max = 60)
        String relacion
) {
}
