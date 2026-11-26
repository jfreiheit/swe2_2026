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

    public User(){}

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole()
    {
        return role;
    }
    public void setRole(Role role)
    {
        this.role = role;
    }
    public Long getId() { return id; }
}
