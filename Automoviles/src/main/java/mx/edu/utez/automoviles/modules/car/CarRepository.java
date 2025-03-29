package mx.edu.utez.automoviles.modules.car;

import mx.edu.utez.automoviles.modules.brand.Brand;
import mx.edu.utez.automoviles.modules.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

        // Búsquedas básicas
        Optional<Car> findCarByModelContaining(String model);
        List<Car> findCarByColor(String color);
        List<Car> findCarByBrand(Brand brand);

        // Búsquedas por ID de relaciones
        List<Car> findByCustomer_Id(Long customerId);
        List<Car> findByBrand_Id(Long brandId);

        // Métodos para los nuevos campos (actualizados a imageName)
        List<Car> findByStatus(String status);
        List<Car> findByImageNameIsNotNull();  // Cambiado de imageUrl a imageName
        List<Car> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

        // Búsquedas combinadas
        List<Car> findByBrand_IdAndStatus(Long brandId, String status);
        List<Car> findByCustomer_IdAndStatus(Long customerId, String status);

        // Métodos para actualizaciones específicas (actualizados)
        @Modifying
        @Query("UPDATE Car c SET c.status = :status WHERE c.id = :id")
        int updateStatus(@Param("id") Long id, @Param("status") String status);

        @Modifying
        @Query("UPDATE Car c SET c.imageName = :imageName WHERE c.id = :id")  // Cambiado imageUrl a imageName
        int updateImageUrl(@Param("id") Long id, @Param("imageName") String imageName);  // Renombrado parámetro

        @Modifying
        @Query("UPDATE Car c SET c.customer = :customer, c.status = 'ASIGNADO', c.saleDate = CURRENT_TIMESTAMP WHERE c.id = :id")
        int assignCustomer(@Param("id") Long id, @Param("customer") Customer customer);

        @Modifying
        @Query("UPDATE Car c SET c.customer = null, c.status = 'DISPONIBLE', c.saleDate = null WHERE c.id = :id")
        int unassignCustomer(@Param("id") Long id);

        // Consultas avanzadas
        @Query("SELECT c FROM Car c WHERE " +
                "(:model IS NULL OR c.model LIKE %:model%) AND " +
                "(:color IS NULL OR c.color = :color) AND " +
                "(:brandId IS NULL OR c.brand.id = :brandId) AND " +
                "(:status IS NULL OR c.status = :status) AND " +
                "(:minPrice IS NULL OR c.purchasePrice >= :minPrice) AND " +
                "(:maxPrice IS NULL OR c.purchasePrice <= :maxPrice)")
        List<Car> searchCars(
                @Param("model") String model,
                @Param("color") String color,
                @Param("brandId") Long brandId,
                @Param("status") String status,
                @Param("minPrice") BigDecimal minPrice,
                @Param("maxPrice") BigDecimal maxPrice
        );

        // Consulta para autos disponibles con imagen (actualizada)
        @Query("SELECT c FROM Car c WHERE c.status = 'DISPONIBLE' AND c.imageName IS NOT NULL ORDER BY c.registrationDate DESC")
        List<Car> findAvailableCarsWithImages();  // Cambiado imageUrl a imageName
}