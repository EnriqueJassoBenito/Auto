package mx.edu.utez.automoviles.modules.customer;

import mx.edu.utez.automoviles.modules.customer.dto.CustomerDTO;
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
@RequestMapping("api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @CrossOrigin(origins = "*")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getCustomer(@PathVariable long id) {
        Optional<CustomerDTO> customerDTO = customerService.getCustomerById(id);

        if (customerDTO.isPresent()) {
            return ResponseEntity.ok(customerDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cliente no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateCustomer(@PathVariable long id, @RequestBody CustomerDTO customerDetails) {
        try {
            CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDetails);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("Cliente eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}