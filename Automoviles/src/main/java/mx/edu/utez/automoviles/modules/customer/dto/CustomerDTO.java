package mx.edu.utez.automoviles.modules.customer.dto;

import jakarta.validation.constraints.*;

public class CustomerDTO {

    private long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String surname;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe ser un número válido (10 a 15 dígitos, opcionalmente con + al inicio)")
    private String phone;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "El correo electrónico debe ser válido")
    private String email;

    private String username;
    private String password;

    @NotNull(message = "El ID del rol no puede estar vacío")
    @Positive(message = "El ID del rol debe ser un número positivo")
    private Long employeeId; // 🔹 Ahora es Long y tiene validaciones correctas

    public CustomerDTO() {
    }

    public CustomerDTO(long id, String name, String surname, String phone, String email, String username, String password, Long employeeId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        this.employeeId = employeeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}