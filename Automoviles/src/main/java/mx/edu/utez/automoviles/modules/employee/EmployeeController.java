package mx.edu.utez.automoviles.modules.employee;

import mx.edu.utez.automoviles.modules.employee.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Obtener todos los empleados
    @GetMapping
    @CrossOrigin(origins = "*")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // Obtener un empleado por ID
    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getEmployeeById(@PathVariable long id) {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);

        if (employeeDTO.isPresent()) {
            return ResponseEntity.ok(employeeDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Empleado no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // Crear un nuevo empleado
    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Actualizar un empleado
    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDetails) {
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Eliminar un empleado
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Empleado eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}