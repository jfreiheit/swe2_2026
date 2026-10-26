package htw.freiheit.uebung_a.dto;

import htw.freiheit.uebung_a.entity.User;

public record UserRequestDTO(String email, String password)
{
    public User toUser()
    {
        User create = new User();
        create.setEmail(this.email);
        create.setPassword(this.password);
        return create;
    }
}
