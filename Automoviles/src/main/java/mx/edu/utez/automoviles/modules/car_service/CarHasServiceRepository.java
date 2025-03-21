package mx.edu.utez.automoviles.modules.car_service;

import mx.edu.utez.automoviles.modules.car.Car;
import mx.edu.utez.automoviles.modules.service.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarHasServiceRepository extends JpaRepository<CarHasService, Long> {

    List<CarHasService> findByCar(Car car);

    List<CarHasService> findByService(Services service);
}