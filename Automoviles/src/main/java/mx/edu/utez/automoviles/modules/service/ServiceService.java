package mx.edu.utez.automoviles.modules.service;

import mx.edu.utez.automoviles.modules.service.dto.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    // Convertir Services → ServiceDTO
    private ServiceDTO convertToDTO(Services service) {
        return new ServiceDTO(service.getId(),
                service.getCode(),
                service.getName(),
                service.getDescription(),
                service.getPrice());
    }

    // Convertir ServiceDTO → Services
    private Services convertToEntity(ServiceDTO serviceDTO) {
        return new Services(
                serviceDTO.getId() != null ? serviceDTO.getId() : null,
                serviceDTO.getCode(),
                serviceDTO.getName(),
                serviceDTO.getDescription(),
                serviceDTO.getPrice(),
                null // Se omite la lista de CarHasService
        );
    }


    @Transactional
    public ServiceDTO saveService(ServiceDTO serviceDTO) {
        if (serviceRepository.existsByCode(serviceDTO.getCode())) {
            throw new RuntimeException("El código del servicio ya está en uso.");
        }
        Services service = convertToEntity(serviceDTO);
        Services savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    @Transactional(readOnly = true)
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ServiceDTO> getServiceById(Long id) {
        return serviceRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public ServiceDTO updateService(Long id, ServiceDTO serviceDetails) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        service.setCode(serviceDetails.getCode());
        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setPrice(serviceDetails.getPrice());

        return convertToDTO(serviceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado");
        }
        serviceRepository.deleteById(id);
    }
}