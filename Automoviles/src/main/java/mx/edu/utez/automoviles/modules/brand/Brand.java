package mx.edu.utez.automoviles.modules.brand;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import mx.edu.utez.automoviles.modules.car.Car;

import java.util.List;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_brand", nullable = false)
    private long id;

    @NotNull(message = "El nombre de la marca no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]{4,50}$", message = "El nombre debe tener entre 4 y 50 caracteres y solo puede contener letras y espacios")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Car> cars;

    // Constructor por defecto
    public Brand() {
    }

    // Constructor que acepta solo el nombre
    public Brand(String name) {
        this.name = name;
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}