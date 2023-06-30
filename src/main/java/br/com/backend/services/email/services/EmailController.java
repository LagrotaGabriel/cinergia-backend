package br.com.backend.services.email.services;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @PostMapping
    public ResponseEntity<Boolean> validaEmail(@RequestBody Email email) {
        return ResponseEntity.ok().build();
    }

}
