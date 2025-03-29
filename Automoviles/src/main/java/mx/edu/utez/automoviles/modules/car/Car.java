package mx.edu.utez.automoviles.modules.car;

import jakarta.persistence.*;
import mx.edu.utez.automoviles.modules.brand.Brand;
import mx.edu.utez.automoviles.modules.car_service.CarHasService;
import mx.edu.utez.automoviles.modules.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_car", nullable = false)
    private Long id;

    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "id_brand", nullable = false)
    private Brand brand;

    @Column(name = "color", length = 30, nullable = false)
    private String color;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "purchase_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = true) // ✅ Correcto
    private Customer customer;

    @Column(name = "image_name")
    private String imageName; // Almacenará el nombre único del archivo

    @Column(name = "status", length = 20)
    private String status = "DISPONIBLE";  // DISPONIBLE|ASIGNADO|VENDIDO

    @Column(name = "sale_date")
    private LocalDateTime saleDate;  // Fecha de venta/asignación

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<CarHasService> services;

    // Constructores
    public Car() {
    }

    public Car(Long id, String model, Brand brand, String color, LocalDateTime registrationDate,
               BigDecimal purchasePrice, Customer customer, String imageName, String status,
               LocalDateTime saleDate, List<CarHasService> services) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.registrationDate = registrationDate;
        this.purchasePrice = purchasePrice;
        this.customer = customer;
        this.imageName = imageName;
        this.status = status;
        this.saleDate = saleDate;
        this.services = services;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    // Añade estos nuevos métodos
    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public List<CarHasService> getServices() {
        return services;
    }

    public void setServices(List<CarHasService> services) {
        this.services = services;
    }

    // Métodos auxiliares
    public void assignToCustomer(Customer customer) {
        this.customer = customer;
        this.status = "ASIGNADO";
        this.saleDate = LocalDateTime.now();
    }

    public void markAsSold() {
        this.status = "VENDIDO";
        this.saleDate = LocalDateTime.now();
    }


    // Método helper para obtener la URL completa
    public String getImageUrl() {
        return this.imageName != null ?
                "/api/cars/images/" + this.imageName : null;
    }
}