package mx.edu.utez.automoviles.modules.car_service;

import mx.edu.utez.automoviles.modules.car_service.dto.CarHasServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/car_has_services")
public class CarHasServiceController {

    @Autowired
    private CarHasServiceService carHasServiceService;

    @GetMapping
    @CrossOrigin(origins = "*")
    public List<CarHasServiceDTO> getAllCarHasServices() {
        return carHasServiceService.getAllCarHasServices();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getCarHasServiceById(@PathVariable long id) {
        Optional<CarHasServiceDTO> carHasServiceDTO = carHasServiceService.getCarHasServiceById(id);

        if (carHasServiceDTO.isPresent()) {
            return ResponseEntity.ok(carHasServiceDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Carro con servicios no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addCarHasService(@RequestBody CarHasServiceDTO carHasServiceDTO) {
        try {
            CarHasServiceDTO savedCarHasServiceDTO = carHasServiceService.saveCarHasService(carHasServiceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCarHasServiceDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteCarHasService(@PathVariable Long id) {
        try {
            carHasServiceService.deleteCarHasService(id);
            return ResponseEntity.ok("Registro de CarHasService eliminado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}