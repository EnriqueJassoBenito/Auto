package mx.edu.utez.automoviles.modules.brand;

import jakarta.validation.Valid;
import mx.edu.utez.automoviles.modules.brand.dto.BrandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    @CrossOrigin(origins = "*") // CORS para GET
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*") // CORS para GET por ID
    public ResponseEntity<?> getBrandById(@PathVariable Long id) {
        Optional<BrandDTO> brandDTO = brandService.getBrandById(id);
        if (brandDTO.isPresent()) {
            return ResponseEntity.ok(brandDTO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Marca no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @CrossOrigin(origins = "*") // CORS para POST
    public ResponseEntity<?> addBrand(@Valid @RequestBody BrandDTO brandDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Manejo de errores de validación
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            BrandDTO savedBrand = brandService.saveBrand(brandDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*") // CORS para PUT
    public ResponseEntity<?> updateBrand(@PathVariable long id, @Valid @RequestBody BrandDTO brandDTO) {
        return brandService.updateBrand(id, brandDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body((BrandDTO) Map.of("error", "Marca no encontrada")));
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*") // CORS para DELETE
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        if (brandService.deleteBrand(id)) {
            return ResponseEntity.ok("Marca eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marca no encontrada");
        }
    }
}