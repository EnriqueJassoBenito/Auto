package mx.edu.utez.automoviles.modules.car;

import jakarta.validation.Valid;
import mx.edu.utez.automoviles.modules.car.dto.CarDTO;
import mx.edu.utez.automoviles.modules.car.dto.CarStatusDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
public class CarController {
    private final CarService carService;
    private final FileStorageService fileStorageService;

    public CarController(CarService carService, FileStorageService fileStorageService) {
        this.carService = carService;
        this.fileStorageService = fileStorageService;
    }

    // Métodos CRUD completos
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAll() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<CarDTO> car = carService.findById(id);
            if (car.isPresent()) {
                return ResponseEntity.ok(car.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "status", HttpStatus.NOT_FOUND.value(),
                                "error", "Not Found",
                                "message", "El auto con ID " + id + " no fue encontrado",
                                "timestamp", LocalDateTime.now()
                        ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Internal Server Error",
                            "message", "Ocurrió un error al procesar la solicitud",
                            "timestamp", LocalDateTime.now()
                    ));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CarDTO carDTO) {
        try {
            CarDTO savedCar = carService.save(carDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CarDTO carDTO) {
        try {
            return ResponseEntity.ok(carService.update(id, carDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            carService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Auto eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Métodos de operaciones especiales
    @PatchMapping("/{id}/assign")
    public ResponseEntity<?> assignCustomer(
            @PathVariable Long id,
            @RequestParam Long customerId) {
        try {
            return ResponseEntity.ok(carService.assignCustomer(id, customerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody CarStatusDTO statusDTO) {
        try {
            return ResponseEntity.ok(carService.updateStatus(id, statusDTO.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarDTO>> getAvailableCars() {
        return ResponseEntity.ok(carService.findByStatus("DISPONIBLE"));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CarDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(carService.findByStatus(status));
    }

    // Métodos de manejo de imágenes
    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }

            String filename = fileStorageService.storeFile(file);
            CarDTO updatedCar = carService.updateImage(id, filename);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida correctamente",
                    "filename", filename,
                    "imageUrl", "/api/cars/" + id + "/image",
                    "car", updatedCar
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error al subir imagen",
                            "details", e.getMessage()
                    ));
        }
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> serveImage(@PathVariable Long id) {
        try {
            String filename = carService.getImageFilename(id);
            Resource file = fileStorageService.loadFileAsResource(filename);

            String contentType = Files.probeContentType(file.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        try {
            String filename = carService.getImageFilename(id);
            fileStorageService.deleteFile(filename);
            carService.updateImage(id, null);

            return ResponseEntity.ok(Map.of(
                    "message", "Imagen eliminada correctamente",
                    "carId", id
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Error al eliminar imagen",
                    "details", e.getMessage()
            ));
        }
    }
}