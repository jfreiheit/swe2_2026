package htw.freiheit.first.controller;

import htw.freiheit.first.dto.RoleRequestDTO;
import htw.freiheit.first.dto.RoleResponseDTO;
import htw.freiheit.first.entity.Role;
import htw.freiheit.first.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController
{
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleRequestDTO role)
    {
        if (roleRepository.existsByRole(role.role()))
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Role roleEntity = new Role(role.role());
        Role saved = roleRepository.save(roleEntity);
        RoleResponseDTO response = new RoleResponseDTO(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles()
    {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponseDTO> response = roles
                .stream()
                .map(RoleResponseDTO::new)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
