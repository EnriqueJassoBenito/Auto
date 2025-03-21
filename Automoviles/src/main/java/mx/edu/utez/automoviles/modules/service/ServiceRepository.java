package mx.edu.utez.automoviles.modules.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    Optional<Services> findByCode(String code);

    boolean existsByCode(String code);
}
