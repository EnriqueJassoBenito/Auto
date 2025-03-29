package mx.edu.utez.automoviles.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.automoviles.modules.employee.Employee;
import mx.edu.utez.automoviles.modules.employee.EmployeeRepository;
import mx.edu.utez.automoviles.security.MainSecurity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final EmployeeRepository employeeRepository;
    private final Set<String> whiteList;

    public AuthFilter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.whiteList = Arrays.stream(MainSecurity.getWHITE_LIST())
                .collect(Collectors.toSet());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Si la ruta está en la WHITE_LIST, ignorar el filtro
        if (whiteList.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Verificar el header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "Token no proporcionado", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 3. Extraer y normalizar el token
        String token = authHeader.substring(7).trim(); // Elimina "Bearer " y espacios
        String[] tokenParts = token.split("\\.");

        // 4. Validar formato del token (debe ser: id.username.rol)
        if (tokenParts.length != 3) {  // Cambiado de 4 a 3 partes
            sendError(response, "Formato de token inválido. Debe ser: id.username.rol", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String id = tokenParts[0];      // Primera parte: id (antes era [1])
        String username = tokenParts[1]; // Segunda parte: username (antes era [2])
        String role = tokenParts[2];    // Tercera parte: rol (antes era [3])

        // 5. Buscar empleado en la base de datos
        Employee employee = employeeRepository.findByUsername(username)
                .orElse(null);

        if (employee == null || !String.valueOf(employee.getId()).equals(id)) {
            sendError(response, "Credenciales inválidas", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 6. Verificar rol (opcional, si quieres validar contra la BD)
        if (!employee.getRole().getName().equalsIgnoreCase(role)) {
            sendError(response, "Rol no coincide", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 7. Establecer autenticación en el contexto de seguridad
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 8. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(
                String.format("{\"error\": \"%s\", \"status\": %d}", message, status)
        );
    }
}