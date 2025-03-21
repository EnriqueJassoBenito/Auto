package mx.edu.utez.automoviles.modules.car_service;

import mx.edu.utez.automoviles.modules.car.Car;
import mx.edu.utez.automoviles.modules.car.CarRepository;
import mx.edu.utez.automoviles.modules.car_service.dto.CarHasServiceDTO;
import mx.edu.utez.automoviles.modules.service.ServiceRepository;
import mx.edu.utez.automoviles.modules.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarHasServiceService {

    @Autowired
    private CarHasServiceRepository carHasServiceRepository;

    @Autowired
    private CarRepository carRepository; // Asegúrate de tener acceso al repositorio de Car
    @Autowired
    private ServiceRepository servicesRepository; // Asegúrate de tener acceso al repositorio de Services

    // Convertir CarHasService → CarHasServiceDTO
    private CarHasServiceDTO convertToDTO(CarHasService carHasService) {
        return new CarHasServiceDTO(carHasService.getId(),
                carHasService.getCar().getId(),
                carHasService.getService().getId());
    }

    // Convertir CarHasServiceDTO → CarHasService
    private CarHasService convertToEntity(CarHasServiceDTO dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        Services service = servicesRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found")); // 🔹 Ensure service exists

        return new CarHasService(dto.getId(), car, service);
    }


    @Transactional
    public CarHasServiceDTO saveCarHasService(CarHasServiceDTO carHasServiceDTO) {
        CarHasService carHasService = convertToEntity(carHasServiceDTO);
        CarHasService savedCarHasService = carHasServiceRepository.save(carHasService);
        return convertToDTO(savedCarHasService);
    }

    @Transactional(readOnly = true)
    public List<CarHasServiceDTO> getAllCarHasServices() {
        return carHasServiceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CarHasServiceDTO> getCarHasServiceById(Long id) {
        return carHasServiceRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public void deleteCarHasService(Long id) {
        if (!carHasServiceRepository.existsById(id)) {
            throw new RuntimeException("Registro de CarHasService no encontrado.");
        }
        carHasServiceRepository.deleteById(id);
    }
}