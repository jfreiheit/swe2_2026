package htw.freiheit.uebung_a.controller;

import htw.freiheit.uebung_a.dto.RoleResponseDTO;
import htw.freiheit.uebung_a.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> result = List.of(
                new UserResponseDTO("user1@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("user2@test.com", new RoleResponseDTO("user")),
                new UserResponseDTO("admin@test.com", new RoleResponseDTO("admin"))
        );
        return ResponseEntity.ok().body(result);
    }
}
