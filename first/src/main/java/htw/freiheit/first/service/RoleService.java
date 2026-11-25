package htw.freiheit.first.service;

import htw.freiheit.first.dto.RoleRequestDTO;
import htw.freiheit.first.dto.RoleResponseDTO;
import htw.freiheit.first.entity.Role;
import htw.freiheit.first.exception.ConflictException;
import htw.freiheit.first.exception.ResourceNotFoundException;
import htw.freiheit.first.repository.RoleRepository;
import htw.freiheit.first.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public RoleResponseDTO create(RoleRequestDTO request) {
        Role role = new Role(request.role());
        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleService::toResponse)
                .toList();
    }

    public RoleResponseDTO findById(Long id) {
        Role role = getRoleOrThrow(id);
        return toResponse(role);
    }

    public void delete(Long id) {
        Role role = getRoleOrThrow(id);
        if (userRepository.existsByRoleId(role.getId())) {
            throw new ConflictException(
                    "Role wird noch von Nutzern verwendet und kann nicht gelöscht werden: " + id);
        }
        roleRepository.delete(role);
    }

    private Role getRoleOrThrow(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    public static RoleResponseDTO toResponse(Role role) {

        return new RoleResponseDTO(role.getId(), role.getRole());
    }
}
