package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.dto.UserRequestDTO;
import htw.freiheit.studi.dto.UserResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.entity.User;
import htw.freiheit.studi.exception.ConflictException;
import htw.freiheit.studi.exception.ResourceNotFoundException;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new ConflictException("username bereits vergeben: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("email bereits vergeben: " + request.email());
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

    public UserResponseDTO findById(Long id) {
        return toResponse(getUserOrThrow(id));
    }

    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = getUserOrThrow(id);

        if (userRepository.existsByUsernameAndIdNot(request.username(), id)) {
            throw new ConflictException("username bereits vergeben: " + request.username());
        }
        if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("email bereits vergeben: " + request.email());
        }
        Role role = getRoleOrThrow(request.roleId());

        user.setUsername(request.username());
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

    private Role getRoleOrThrow(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public static UserResponseDTO toResponse(User user) {
        RoleResponseDTO roleDTO = RoleService.toResponse(user.getRole());
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), roleDTO);
    }
}
