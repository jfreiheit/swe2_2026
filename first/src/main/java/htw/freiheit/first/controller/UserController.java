package htw.freiheit.first.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello FIW!", HttpStatus.OK);
    }

    @GetMapping("/fiw")
    public String hello2() {
        return "nur FIW!";
    }
}
