package mx.edu.utez.automoviles.modules.service;

import jakarta.persistence.*;
import mx.edu.utez.automoviles.modules.car_service.CarHasService;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service", nullable = false)
    private Long id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "service")
    private List<CarHasService> carHasServiceDTOs;

    public Services() {
    }

    public Services(Long id, String code, String name, String description, BigDecimal price, List<CarHasService> carHasServiceDTOs) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.carHasServiceDTOs = carHasServiceDTOs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<CarHasService> getCarHasServiceDTOs() {
        return carHasServiceDTOs;
    }

    public void setCarHasServiceDTOs(List<CarHasService> carHasServiceDTOs) {
        this.carHasServiceDTOs = carHasServiceDTOs;
    }
}
