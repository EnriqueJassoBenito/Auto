package mx.edu.utez.automoviles.modules.brand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BrandDTO {

    private Long id;

    @NotNull(message = "El nombre de la marca no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{4,50}$", message = "El nombre debe tener entre 4 y 50 caracteres y solo puede contener letras y espacios")
    private String name;

    public BrandDTO() {}

    public BrandDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
