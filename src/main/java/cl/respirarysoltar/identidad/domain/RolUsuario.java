package cl.respirarysoltar.identidad.domain;

/**
 * Roles de negocio de la plataforma. Las credenciales y MFA viven en AWS Cognito;
 * aquí solo se persisten los datos de perfil y el rol funcional (RF-01).
 */
public enum RolUsuario {
    PACIENTE,
    PSICOLOGO,
    ADMIN
}
