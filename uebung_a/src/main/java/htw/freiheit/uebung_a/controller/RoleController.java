package htw.freiheit.uebung_a.controller;

import htw.freiheit.uebung_a.dto.RoleRequestDTO;
import htw.freiheit.uebung_a.entity.Role;
import htw.freiheit.uebung_a.repository.RoleRepository;
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
        if(this.roleRepository.existsByRole(roleRequestDTO.role()))
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Role create = new Role();
        create.setRole(roleRequestDTO.role());
        Role saved = this.roleRepository.save(create);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
