package mx.edu.utez.automoviles.modules.car.dto;

import jakarta.validation.constraints.*;
import mx.edu.utez.automoviles.modules.brand.dto.BrandDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarDTO {

    private long id;

    @NotBlank(message = "El modelo del auto no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]{2,50}$", message = "El modelo debe tener entre 2 y 50 caracteres y solo puede contener letras, números y espacios")
    private String model;

    @NotBlank(message = "El color del auto no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z\\s]{3,30}$", message = "El color debe tener entre 3 y 30 caracteres y solo puede contener letras y espacios")
    private String color;

    @NotNull(message = "La fecha de registro no puede estar vacía")
    private LocalDateTime registrationDate;

    @NotNull(message = "El precio de compra no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "El ID de la marca no puede estar vacío")
    @Positive(message = "El ID de la marca debe ser un número positivo")
    private Long brandId; //Se usa el ID en lugar de un objeto BrandDTO

    @NotNull(message = "El ID del cliente no puede estar vacío")
    @Positive(message = "El ID del cliente debe ser un número positivo")
    private Long customerId; // Se usa el ID en lugar de un objeto CustomerDTO

    public CarDTO() {
    }

    public CarDTO(long id, String model, String color, LocalDateTime registrationDate, BigDecimal purchasePrice, Long brandId, Long customerId) {
        this.id = id;
        this.model = model;
        this.color = color;
        this.registrationDate = registrationDate;
        this.purchasePrice = purchasePrice;
        this.brandId = brandId;
        this.customerId = customerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
