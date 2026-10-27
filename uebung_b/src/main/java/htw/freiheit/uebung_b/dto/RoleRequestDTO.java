package htw.freiheit.uebung_b.dto;

import htw.freiheit.uebung_b.entity.Role;

public record RoleRequestDTO(String role)
{
    public Role toRole()
    {
        Role role = new Role();
        role.setRole(this.role);
        return role;
    }
}
