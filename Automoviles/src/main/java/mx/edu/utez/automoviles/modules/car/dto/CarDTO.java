package mx.edu.utez.automoviles.modules.car.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarDTO {
    private Long id;
    private String model;
    private Long brandId;
    private String color;
    private LocalDateTime registrationDate;
    private BigDecimal purchasePrice;
    private Long customerId;
    private String imageName;
    private String status;
    private LocalDateTime saleDate;

    public CarDTO() {
    }

    public CarDTO(Long id, String model, Long brandId, String color,
                  LocalDateTime registrationDate, BigDecimal purchasePrice,
                  Long customerId, String imageName, String status,
                  LocalDateTime saleDate) {
        this.id = id;
        this.model = model;
        this.brandId = brandId;
        this.color = color;
        this.registrationDate = registrationDate;
        this.purchasePrice = purchasePrice;
        this.customerId = customerId;
        this.imageName = imageName;
        this.status = status;
        this.saleDate = saleDate;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
}