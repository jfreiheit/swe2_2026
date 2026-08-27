package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.RoleRequestDTO;
import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.repository.RoleRepository;
import htw.freiheit.studi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Role role = new Role(request.name());
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
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Role wird noch von Nutzern verwendet und kann nicht gelöscht werden: " + id);
        }
        roleRepository.delete(role);
    }

    private Role getRoleOrThrow(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role nicht gefunden: " + id));
    }

    public static RoleResponseDTO toResponse(Role role) {
        return new RoleResponseDTO(role.getId(), role.getName());
    }
}
