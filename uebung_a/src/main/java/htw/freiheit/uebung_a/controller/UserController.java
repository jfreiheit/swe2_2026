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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> result = List.of(
                new UserResponseDTO("user1@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("user2@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("admin@test.com", new RoleResponseDTO("admin"))
        );
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        Optional<Role> role = roleRepository.findByRole("user");
        if (role.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User createUser = userRequestDTO.toUser();
        createUser.setRole(role.get());
        User saved = userRepository.save(createUser);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
