package htw.freiheit.first.dto;

public record UserResponseDTO(Long id,
                              String account,
                              String email,
                              RoleResponseDTO role)
{
}
