package mx.edu.utez.automoviles.modules.car;

import jakarta.persistence.*;
import mx.edu.utez.automoviles.modules.brand.Brand;
import mx.edu.utez.automoviles.modules.car_service.CarHasService;
import mx.edu.utez.automoviles.modules.customer.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_car", nullable = false)
    private long id;

    @Column(name = "model", length = 50, nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "id_brand", nullable = false)
    private Brand brand;

    @Column(name = "color", length = 30, nullable = false)
    private String color;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "purchase_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<CarHasService> services;

    public Car() {
    }

    public Car(long id, String model, Brand brand, String color, LocalDateTime registrationDate, BigDecimal purchasePrice, Customer customer, List<CarHasService> services) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.registrationDate = registrationDate;
        this.purchasePrice = purchasePrice;
        this.customer = customer;
        this.services = services;
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

    public List<CarHasService> getServices() {
        return services;
    }

    public void setServices(List<CarHasService> services) {
        this.services = services;
    }
}
