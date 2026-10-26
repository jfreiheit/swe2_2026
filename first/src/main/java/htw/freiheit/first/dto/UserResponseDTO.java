package htw.freiheit.first.dto;

public record UserResponseDTO(Long id,
                              String account,
                              String email,
                              String password,
                              RoleResponseDTO role)
{
}
