package mx.edu.utez.automoviles.modules.car.dto;

import jakarta.validation.constraints.Pattern;

public class CarStatusDTO {
    @Pattern(regexp = "^(DISPONIBLE|ASIGNADO|VENDIDO)$",
            message = "Estado inválido. Valores permitidos: DISPONIBLE, ASIGNADO, VENDIDO")
    private String status;

    // Getters y Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}