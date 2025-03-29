package mx.edu.utez.automoviles.modules.car;

import jakarta.transaction.Transactional;
import mx.edu.utez.automoviles.modules.brand.Brand;
import mx.edu.utez.automoviles.modules.brand.BrandRepository;
import mx.edu.utez.automoviles.modules.car.dto.CarDTO;
import mx.edu.utez.automoviles.modules.customer.Customer;
import mx.edu.utez.automoviles.modules.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CarService(CarRepository carRepository,
                      BrandRepository brandRepository,
                      CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.brandRepository = brandRepository;
        this.customerRepository = customerRepository;
    }

    // Conversiones actualizadas
    private CarDTO convertToDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getModel(),
                car.getBrand() != null ? car.getBrand().getId() : null,
                car.getColor(),
                car.getRegistrationDate(),
                car.getPurchasePrice(),
                car.getCustomer() != null ? car.getCustomer().getId() : null,
                car.getImageName(),  // Cambiado a getImageName()
                car.getStatus(),
                car.getSaleDate()
        );
    }

    private Car convertToEntity(CarDTO carDTO) {
        Brand brand = brandRepository.findById(carDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Customer customer = carDTO.getCustomerId() != null ?
                customerRepository.findById(carDTO.getCustomerId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado")) :
                null;

        return new Car(
                carDTO.getId(),
                carDTO.getModel(),
                brand,
                carDTO.getColor(),
                carDTO.getRegistrationDate(),
                carDTO.getPurchasePrice(),
                customer,
                carDTO.getImageName(),  // Cambiado a getImageName()
                carDTO.getStatus(),
                carDTO.getSaleDate(),
                null
        );
    }

    // Operaciones CRUD actualizadas
    @Transactional
    public CarDTO save(CarDTO carDTO) {
        Car car = convertToEntity(carDTO);
        if (carDTO.getCustomerId() == null) {
            car.setCustomer(null);
        }
        car.setStatus("DISPONIBLE");
        car.setRegistrationDate(LocalDateTime.now());
        return convertToDTO(carRepository.save(car));
    }

    @Transactional
    public List<CarDTO> findAll() {
        return carRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<CarDTO> findById(Long id) {
        return carRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public CarDTO update(Long id, CarDTO carDTO) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        existingCar.setModel(carDTO.getModel());
        existingCar.setColor(carDTO.getColor());
        existingCar.setPurchasePrice(carDTO.getPurchasePrice());
        existingCar.setImageName(carDTO.getImageName());  // Cambiado a setImageName()

        if (carDTO.getBrandId() != null) {
            Brand brand = brandRepository.findById(carDTO.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            existingCar.setBrand(brand);
        }

        return convertToDTO(carRepository.save(existingCar));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Auto no encontrado");
        }
        carRepository.deleteById(id);
    }

    // Método para manejo de imágenes
    @Transactional
    public String getImageFilename(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        if (car.getImageName() == null || car.getImageName().isEmpty()) {
            throw new RuntimeException("El auto no tiene una imagen asociada");
        }

        return car.getImageName();  // Usando getImageName()
    }

    // Operaciones específicas actualizadas
    @Transactional
    public CarDTO updateImage(Long id, String imageName) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        car.setImageName(imageName);
        return convertToDTO(carRepository.save(car));
    }

    @Transactional
    public CarDTO assignCustomer(Long carId, Long customerId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        car.setCustomer(customer);
        car.setStatus("ASIGNADO");
        car.setSaleDate(LocalDateTime.now());

        return convertToDTO(carRepository.save(car));
    }

    @Transactional
    public CarDTO unassignCustomer(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        car.setCustomer(null);
        car.setStatus("DISPONIBLE");
        car.setSaleDate(null);

        return convertToDTO(carRepository.save(car));
    }

    @Transactional
    public CarDTO updateStatus(Long id, String status) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado"));

        if (!List.of("DISPONIBLE", "ASIGNADO", "VENDIDO").contains(status)) {
            throw new RuntimeException("Estado no válido");
        }

        car.setStatus(status);
        if (status.equals("VENDIDO")) {
            car.setSaleDate(LocalDateTime.now());
        }

        return convertToDTO(carRepository.save(car));
    }

    @Transactional
    public List<CarDTO> findByStatus(String status) {
        return carRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}