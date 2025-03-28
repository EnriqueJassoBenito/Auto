package mx.edu.utez.automoviles.modules.auth;

import mx.edu.utez.automoviles.kernel.CustomResponse;
import mx.edu.utez.automoviles.modules.auth.dto.LoginDTO;
import mx.edu.utez.automoviles.modules.employee.Employee;
import mx.edu.utez.automoviles.modules.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private EmployeeRepository employeeRepository; // Cambiamos UserRepository por EmployeeRepository

    @Autowired
    private CustomResponse customResponse;

    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public ResponseEntity<?> login(LoginDTO dto) {
        Optional<Employee> found = employeeRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        if (found.isPresent()) {
            Employee employee = found.get();
            // Formato: <id>.<username>.<rol> (sin prefijos/sufijos)
            String token = String.format("%d.%s.%s",
                    employee.getId(),
                    employee.getUsername(),
                    employee.getRole().getName());
            return customResponse.getOkResponse(token);
        } else {
            return customResponse.get400Response(404);
        }
    }
}