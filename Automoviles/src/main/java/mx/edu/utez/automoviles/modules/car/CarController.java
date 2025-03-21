package mx.edu.utez.automoviles.modules.car;

import jakarta.validation.Valid;
import mx.edu.utez.automoviles.modules.car.dto.CarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }


    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getCarById(@PathVariable long id) {
        Optional<CarDTO> carDTO = carService.getCarById(id);

        if (carDTO.isPresent()) {
            return ResponseEntity.ok(carDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Car no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> addCar(@Valid @RequestBody CarDTO carDTO) {
        try {
            CarDTO savedCar = carService.saveCar(carDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Actualizar un auto
    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @Valid @RequestBody CarDTO carDTO) {
        try {
            CarDTO updatedCar = carService.updateCar(id, carDTO)
                    .orElseThrow(() -> new RuntimeException("Auto no encontrado"));
            return ResponseEntity.ok(updatedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // Eliminar un auto
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (carService.deleteCar(id)) {
            return ResponseEntity.ok("Auto eliminado correctamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auto no encontrado");
    }

}
