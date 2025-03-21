package mx.edu.utez.automoviles.modules.rol;

import mx.edu.utez.automoviles.modules.rol.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Convertir Role → RoleDTO
    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    // Convertir RoleDTO → Role
    private Role convertToEntity(RoleDTO roleDTO) {
        return new Role(roleDTO.getId(), roleDTO.getName(), null); // Asigna la lista de empleados si es necesario
    }

    @Transactional
    public RoleDTO saveRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new RuntimeException("El rol ya existe.");
        }
        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RoleDTO> getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        role.setName(roleDetails.getName());
        return convertToDTO(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        roleRepository.deleteById(id);
    }
}