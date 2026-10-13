package htw.freiheit.first.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String account;
    String email;
    String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public User(){}
}
