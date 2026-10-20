package htw.freiheit.uebung_b.controller;

import htw.freiheit.uebung_b.dto.RoleResponseDTO;
import htw.freiheit.uebung_b.dto.UserResponseDTO;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<UserResponseDTO>> getUser()
    {
        List<UserResponseDTO> result = List.of(
          new UserResponseDTO("user1", "user1@test.de", new RoleResponseDTO("user")),
          new UserResponseDTO("user2", "user2@test.de", new RoleResponseDTO("user")),
          new UserResponseDTO("user3", "user3@test.de", new RoleResponseDTO("admin"))
        );
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
