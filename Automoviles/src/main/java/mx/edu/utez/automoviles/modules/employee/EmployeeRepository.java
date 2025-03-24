package mx.edu.utez.automoviles.modules.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT * FROM employee WHERE username = :username AND password = :password", nativeQuery = true)
    Optional<Employee> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


    @Query(value = "SELECT * FROM employee WHERE username = :username", nativeQuery = true)
    Optional<Employee> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}