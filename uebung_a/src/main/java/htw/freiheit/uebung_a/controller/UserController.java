package htw.freiheit.uebung_a.controller;

import htw.freiheit.uebung_a.dto.RoleResponseDTO;
import htw.freiheit.uebung_a.dto.UserRequestDTO;
import htw.freiheit.uebung_a.dto.UserResponseDTO;
import htw.freiheit.uebung_a.entity.Role;
import htw.freiheit.uebung_a.entity.User;
import htw.freiheit.uebung_a.repository.RoleRepository;
import htw.freiheit.uebung_a.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> result = List.of(
                new UserResponseDTO("user1@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("user2@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("admin@test.com", new RoleResponseDTO("admin"))
        );
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        Optional<Role> role = roleRepository.findByRole("user");
        if (role.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User createUser = new User();
        createUser.setEmail(userRequestDTO.email());
        createUser.setRole(role.get());

        String hashedPassword = passwordEncoder.encode(userRequestDTO.password());
        createUser.setPassword(hashedPassword);

        User saved = userRepository.save(createUser);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        Optional<User> savedUser = userRepository.findByEmail(userRequestDTO.email());
        if (savedUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = savedUser.get(); // Nutzerin mit Email gefunden
        String hashedPassword = user.getPassword();  // gespeicherte gehashte Passwort
        String clearPassword = userRequestDTO.password(); // Klar-Passwort login

        if (!passwordEncoder.matches(clearPassword, hashedPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);    // Passwoerter passen nicht
        }
        return new ResponseEntity<>(user, HttpStatus.OK); // eingelogged
    }
}
