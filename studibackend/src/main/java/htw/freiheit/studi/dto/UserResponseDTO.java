package htw.freiheit.studi.dto;

public record UserResponseDTO(Long id, String username, String email, RoleResponseDTO role) {
}
