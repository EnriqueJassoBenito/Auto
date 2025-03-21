package mx.edu.utez.automoviles.modules.customer;

import mx.edu.utez.automoviles.modules.customer.dto.CustomerDTO;
import mx.edu.utez.automoviles.modules.employee.Employee;
import mx.edu.utez.automoviles.modules.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Convertir Customer → CustomerDTO
    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getEmployee() != null ? customer.getEmployee().getId() : null); // 🔹 Incluir employeeId
    }


    // Convertir CustomerDTO → Customer
    private Customer convertToEntity(CustomerDTO customerDTO) {
        Employee employee = employeeRepository.findById(customerDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado")); // 🔹 Validar si el empleado existe

        return new Customer(customerDTO.getId(),
                customerDTO.getName(),
                customerDTO.getSurname(),
                customerDTO.getPhone(),
                customerDTO.getEmail(),
                customerDTO.getUsername(),
                customerDTO.getPassword(),
                employee,
                null);
    }


    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> getCustomerById(long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO);
    }

    public CustomerDTO updateCustomer(long id, CustomerDTO customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        customer.setName(customerDetails.getName());
        customer.setSurname(customerDetails.getSurname());
        customer.setPhone(customerDetails.getPhone());
        customer.setEmail(customerDetails.getEmail());
        customer.setUsername(customerDetails.getUsername());
        customer.setPassword(customerDetails.getPassword());

        // Si el employeeId es diferente, actualiza el empleado
        if (customerDetails.getEmployeeId() != null && !customerDetails.getEmployeeId().equals(customer.getEmployee().getId())) {
            Employee employee = employeeRepository.findById(customerDetails.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            customer.setEmployee(employee);
        }

        return convertToDTO(customerRepository.save(customer));
    }


    public void deleteCustomer(long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("El cliente no encontrado");
        }
        customerRepository.deleteById(id);
    }
}