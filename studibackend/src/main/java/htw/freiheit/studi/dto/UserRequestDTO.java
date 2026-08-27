package htw.freiheit.studi.dto;

public record UserRequestDTO(String username, String password, String email, Long roleId) {
}
