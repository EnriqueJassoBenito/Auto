package mx.edu.utez.automoviles.modules.car;

import mx.edu.utez.automoviles.modules.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

        Optional<Car> findCarByModelContaining(String model);

        List<Car> findCarByColor(String color);

        List<Car> findCarByBrand(Brand brand);

        // Buscar autos por el ID del cliente
        List<Car> findByCustomer_Id(Long customerId);

        // Buscar autos por el ID de la marca
        List<Car> findByBrand_Id(Long brandId);


}
