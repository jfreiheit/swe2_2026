package htw.freiheit.first.service;

import htw.freiheit.first.dto.RoleResponseDTO;
import htw.freiheit.first.dto.UserRequestDTO;
import htw.freiheit.first.dto.UserResponseDTO;
import htw.freiheit.first.entity.Role;
import htw.freiheit.first.entity.User;
import htw.freiheit.first.exception.*;
import htw.freiheit.first.repository.RoleRepository;
import htw.freiheit.first.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create(UserRequestDTO request) {
        if (userRepository.existsByAccount(request.account())) {
            throw new ConflictException("account bereits vergeben: " + request.account());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("email bereits vergeben: " + request.email());
        }
        Optional<Role> roleCheck = roleRepository.findByRole("user");
        Role role;
        if (roleCheck.isEmpty()) {
            role = roleRepository.save(new Role("user"));
        } else {
            role = roleCheck.get();
        }

        User user = new User();
        user.setAccount(request.account());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponseDTO login(UserRequestDTO request) {
        Optional<User> userCheck = Optional.empty();
        if(request.account() != null) {
            userCheck = userRepository.findByAccount(request.account());
        }
        if(userCheck.isEmpty() && request.email() != null)
        {
            userCheck = userRepository.findByEmail(request.email());
        }
        if(userCheck.isEmpty())
        {
            throw new UserNotFoundException("user not found");
        }
        User user = userCheck.get();
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("wrong password");
        }
        return toResponse(user);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserService::toResponse)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        return toResponse(getUserOrThrow(id));
    }

    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = getUserOrThrow(id);

        if (userRepository.existsByAccountAndIdNot(request.account(), id)) {
            throw new ConflictException("username bereits vergeben: " + request.account());
        }
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("email bereits vergeben: " + request.email());
        }
        Role role = getUserRoleOrThrow("user");

        user.setAccount(request.account());
        user.setEmail(request.email());
        user.setRole(role);
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public void delete(Long id) {
        User user = getUserOrThrow(id);
        userRepository.delete(user);
    }

    private Role getUserRoleOrThrow(String role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RoleNotFoundException(role));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public static UserResponseDTO toResponse(User user) {
        RoleResponseDTO roleDTO = RoleService.toResponse(user.getRole());
        return new UserResponseDTO(user.getId(), user.getAccount(), user.getEmail(), roleDTO);
    }
}
