package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.dto.UserResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.entity.User;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username bereits vergeben: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email bereits vergeben: " + request.email());
        }
        Role role = getRoleOrThrow(request.roleId());

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserService::toResponse)
                .toList();
    }

    private Role getRoleOrThrow(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role nicht gefunden: " + roleId));
    }

    public static UserResponseDTO toResponse(User user) {
        RoleResponseDTO roleDTO = RoleService.toResponse(user.getRole());
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), roleDTO);
    }
}
