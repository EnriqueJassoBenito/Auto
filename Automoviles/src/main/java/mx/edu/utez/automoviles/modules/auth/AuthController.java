package mx.edu.utez.automoviles.modules.auth;

import mx.edu.utez.automoviles.modules.auth.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")

public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }
}
