package htw.freiheit.uebung_b.controller;

import htw.freiheit.uebung_b.dto.RoleResponseDTO;
import htw.freiheit.uebung_b.dto.UserRequestDTO;
import htw.freiheit.uebung_b.dto.UserResponseDTO;
import htw.freiheit.uebung_b.entity.Role;
import htw.freiheit.uebung_b.entity.User;
import htw.freiheit.uebung_b.repository.RoleRepository;
import htw.freiheit.uebung_b.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUser()
    {
        List<UserResponseDTO> result = List.of(
          new UserResponseDTO("user1", "user1@test.de", new RoleResponseDTO("user")),
          new UserResponseDTO("user2", "user2@test.de", new RoleResponseDTO("user")),
          new UserResponseDTO("user3", "user3@test.de", new RoleResponseDTO("admin"))
        );
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        Optional<Role> userRole = roleRepository.findByRole("user");
        if (userRole.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(userRepository.existsByEmailOrAccount(userRequestDTO.email(), userRequestDTO.account())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User create = new User();
        create.setEmail(userRequestDTO.email());
        create.setAccount(userRequestDTO.account());
        create.setPassword(userRequestDTO.password());
        create.setRole(userRole.get());
        User saved = this.userRepository.save(create);
        return new ResponseEntity(saved, HttpStatus.CREATED);
    }
}
