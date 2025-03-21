package mx.edu.utez.automoviles.modules.car;

import jakarta.transaction.Transactional;
import mx.edu.utez.automoviles.modules.brand.Brand;
import mx.edu.utez.automoviles.modules.brand.BrandRepository;
import mx.edu.utez.automoviles.modules.brand.dto.BrandDTO;
import mx.edu.utez.automoviles.modules.car.dto.CarDTO;
import mx.edu.utez.automoviles.modules.car_service.CarHasService;
import mx.edu.utez.automoviles.modules.customer.Customer;
import mx.edu.utez.automoviles.modules.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Convertir Car → CarDTO
    private CarDTO convertToDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getModel(),
                car.getColor(),
                car.getRegistrationDate(),
                car.getPurchasePrice(),
                car.getBrand() != null ? car.getBrand().getId() : null, // 🔹 Convertir Brand a brandId
                car.getCustomer() != null ? car.getCustomer().getId() : null // 🔹 Convertir Customer a customerId
        );
    }


    private Car convertToEntity(CarDTO carDTO) {
        Brand brand = brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada")); //Verifica si la marca existe

        Customer customer = customerRepository.findById(carDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")); // Verifica si el cliente existe

        return new Car(
                carDTO.getId(),
                carDTO.getModel(),
                brand, //  Convertir brandId a Brand
                carDTO.getColor(),
                carDTO.getRegistrationDate(),
                carDTO.getPurchasePrice(),
                customer, //Convertir customerId a Customer
                null //Se omite la lista de servicios por ahora
        );
    }


    @Transactional
    public CarDTO saveCar(CarDTO carDTO) {
        Car car = convertToEntity(carDTO);
        Car savedCar = carRepository.save(car);
        return convertToDTO(savedCar);
    }

    @Transactional
    public List<CarDTO> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<CarDTO> getCarById(Long id) {
        return carRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public Optional<CarDTO> updateCar(Long id, CarDTO carDTO) {
        return carRepository.findById(id).map(existingCar -> {
            existingCar.setModel(carDTO.getModel());
            existingCar.setColor(carDTO.getColor());
            existingCar.setRegistrationDate(carDTO.getRegistrationDate());
            existingCar.setPurchasePrice(carDTO.getPurchasePrice());

            return Optional.of(convertToDTO(carRepository.save(existingCar)));
        }).orElse(Optional.empty());
    }

    @Transactional
    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
