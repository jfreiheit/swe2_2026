package htw.freiheit.uebung_b.controller;

import htw.freiheit.uebung_b.dto.RoleRequestDTO;
import htw.freiheit.uebung_b.entity.Role;
import htw.freiheit.uebung_b.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Role> createRole(@RequestBody RoleRequestDTO roleRequestDTO)
    {
        if(roleRepository.existsByRole(roleRequestDTO.role()))
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Role role = roleRequestDTO.toRole();
        Role saved = roleRepository.save(role);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
