package mx.edu.utez.automoviles.modules.rol;

import mx.edu.utez.automoviles.modules.rol.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @CrossOrigin(origins = "*")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        Optional<RoleDTO> roleDTO = roleService.getRoleById(id);

        if (roleDTO.isPresent()) {
            return ResponseEntity.ok(roleDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Rol no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO savedRole = roleService.saveRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDetails) {
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleDetails);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok("Rol eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}