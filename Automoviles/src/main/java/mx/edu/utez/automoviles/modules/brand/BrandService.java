package mx.edu.utez.automoviles.modules.brand;

import jakarta.transaction.Transactional;
import mx.edu.utez.automoviles.modules.brand.dto.BrandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    //@Transactional
    private BrandDTO convertToDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId()); // Aquí se establece el ID después de guardar
        brandDTO.setName(brand.getName());
        return brandDTO;
    }

    // Convertir BrandDTO → Brand
    private Brand convertToEntity(BrandDTO brandDTO) {
        return new Brand(brandDTO.getName()); // No se establece el ID
    }

    @Transactional
    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BrandDTO> getBrandById(Long id) {
        return brandRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public BrandDTO saveBrand(BrandDTO brandDTO) {
        if (brandRepository.existsByName(brandDTO.getName())) {
            throw new RuntimeException("La marca ya existe.");
        }
        Brand brand = convertToEntity(brandDTO);
        Brand savedBrand = brandRepository.save(brand);
        return convertToDTO(savedBrand);
    }

    @Transactional
    public Optional<BrandDTO> updateBrand(Long id, BrandDTO brandDTO) {
        return brandRepository.findById(id).map(existingBrand -> {
            existingBrand.setName(brandDTO.getName());
            Brand updatedBrand = brandRepository.save(existingBrand);
            return convertToDTO(updatedBrand);
        });
    }

    @Transactional
    public boolean deleteBrand(Long id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
