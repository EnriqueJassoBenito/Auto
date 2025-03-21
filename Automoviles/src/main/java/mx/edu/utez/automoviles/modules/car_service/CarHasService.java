package mx.edu.utez.automoviles.modules.car_service;

import jakarta.persistence.*;
import mx.edu.utez.automoviles.modules.car.Car;
import mx.edu.utez.automoviles.modules.service.Services;

@Entity
@Table(name = "car_has_service")
public class CarHasService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_car_has_service", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_car", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "id_service", nullable = false)
    private Services service;

    public CarHasService() {
    }

    public CarHasService(long id, Car car, Services service) {
        this.id = id;
        this.car = car;
        this.service = service;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }
}
