package htw.freiheit.studi.controller;

import htw.freiheit.studi.dto.RoleRequestDTO;
import htw.freiheit.studi.dto.RoleResponseDTO;
import htw.freiheit.studi.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleRequestDTO request) {
        RoleResponseDTO created = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
