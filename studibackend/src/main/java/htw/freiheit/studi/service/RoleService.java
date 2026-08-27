package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.RoleRequestDTO;
import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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

    private Role getRoleOrThrow(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role nicht gefunden: " + id));
    }

    public static RoleResponseDTO toResponse(Role role) {
        return new RoleResponseDTO(role.getId(), role.getName());
    }
}
