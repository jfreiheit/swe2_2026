package htw.freiheit.studi.service;

import htw.freiheit.studi.dto.RoleRequestDTO;
import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.entity.Role;
import htw.freiheit.studi.repository.RoleRepository;
import org.springframework.stereotype.Service;

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

    public static RoleResponseDTO toResponse(Role role) {
        return new RoleResponseDTO(role.getId(), role.getName());
    }
}
