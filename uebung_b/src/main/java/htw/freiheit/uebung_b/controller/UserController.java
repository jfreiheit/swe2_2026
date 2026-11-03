package htw.freiheit.uebung_b.controller;

import htw.freiheit.uebung_b.dto.RoleResponseDTO;
import htw.freiheit.uebung_b.dto.UserRequestDTO;
import htw.freiheit.uebung_b.dto.UserResponseDTO;
import htw.freiheit.uebung_b.entity.Role;
import htw.freiheit.uebung_b.entity.User;
import htw.freiheit.uebung_b.repository.RoleRepository;
import htw.freiheit.uebung_b.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    @RequestMapping("/register")
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

        String passwordInput = userRequestDTO.password();
        String hashedPassword = passwordEncoder.encode(passwordInput);
        create.setPassword(hashedPassword);

        create.setRole(userRole.get());
        User saved = this.userRepository.save(create);
        return new ResponseEntity(saved, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserRequestDTO userRequestDTO)
    {
         Optional<User> searchUser = userRepository.findByEmailOrAccount(
                 userRequestDTO.email(),
                 userRequestDTO.account()
         );
        if(searchUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User userInDB = searchUser.get();
        logger.info("User found in DB: {}", userInDB.getAccount());
        String hashedPassword = userInDB.getPassword();
        String inputPassword = userRequestDTO.password();
        if(passwordEncoder.matches(inputPassword, hashedPassword)) {
            return new ResponseEntity(userInDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
