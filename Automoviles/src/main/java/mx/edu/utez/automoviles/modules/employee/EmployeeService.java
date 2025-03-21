package mx.edu.utez.automoviles.modules.employee;

import mx.edu.utez.automoviles.modules.employee.dto.EmployeeDTO;
import mx.edu.utez.automoviles.modules.rol.Role;
import mx.edu.utez.automoviles.modules.rol.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository; // Asegúrate de tener acceso al repositorio de Role

    // Convertir Employee → EmployeeDTO
    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getName(),
                employee.getSurname(),
                employee.getRole().getId()); // Asumiendo que Role tiene un método getId()
    }

    // Convertir EmployeeDTO → Employee
    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Role role = roleRepository.findById(employeeDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado")); // Manejo de error si el rol no existe

        return new Employee(employeeDTO.getId(),
                employeeDTO.getUsername(),
                employeeDTO.getPassword(),
                employeeDTO.getName(),
                employeeDTO.getSurname(),
                role,
                null); // Asigna la lista de clientes si es necesario
    }

    @Transactional
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        employee.setUsername(employeeDetails.getUsername());
        employee.setPassword(employeeDetails.getPassword());
        employee.setName(employeeDetails.getName());
        employee.setSurname(employeeDetails.getSurname());

        // Actualiza el rol solo si se proporciona un nuevo rol
        Role role = roleRepository.findById(employeeDetails.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        employee.setRole(role);

        return convertToDTO(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado");
        }
        employeeRepository.deleteById(id);
    }
}