package mx.edu.utez.automoviles.modules.car_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CarHasServiceDTO {

    private long id;

    @NotNull(message = "El ID del rol no puede estar vacío")
    @Positive(message = "El ID del rol debe ser un número positivo")
    private Long carId; // ID del automóvil

    @NotNull(message = "El ID del rol no puede estar vacío")
    @Positive(message = "El ID del rol debe ser un número positivo")
    private Long serviceId; // ID del servicio

    public CarHasServiceDTO() {
    }

    public CarHasServiceDTO(long id, Long carId, Long serviceId) {
        this.id = id;
        this.carId = carId;
        this.serviceId = serviceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}