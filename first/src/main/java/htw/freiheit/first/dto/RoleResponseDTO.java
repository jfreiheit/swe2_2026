package htw.freiheit.first.dto;

import htw.freiheit.first.entity.Role;

public record RoleResponseDTO(Long id, String role)
{
    public RoleResponseDTO(Role role)
    {
        this(role.getId(), role.getRole());
    }
}
