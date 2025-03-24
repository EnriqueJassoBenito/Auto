package mx.edu.utez.automoviles.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.automoviles.modules.employee.Employee;
import mx.edu.utez.automoviles.modules.employee.EmployeeRepository;
import mx.edu.utez.automoviles.security.MainSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private EmployeeRepository employeeRepository;

    Set<String> whiteList = Arrays.stream(MainSecurity.getWHITE_LIST()).collect(Collectors.toSet());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String token;
        Employee employee = null;

        if (!whiteList.contains(request.getRequestURI())) {

            System.out.println("MÉTODO DE LA SOLICITUD: " + request.getMethod());
            System.out.println("RUTA SOLICITADA: " + request.getRequestURI());
            System.out.println("VERIFICANDO LOS HEADERS DE LA PETICIÓN");

            if (authHeader != null && authHeader.startsWith("Bearer")) {
                token = authHeader.substring(7); // Eliminar "Bearer " del token

                // Extraer las partes del token
                String[] tokenParts = token.split("\\.");
                if (tokenParts.length >= 4) { // Verificar que el token tenga el formato correcto
                    String id = tokenParts[1];       // El id está en la segunda parte
                    String username = tokenParts[2]; // El username está en la tercera parte
                    String role = tokenParts[3];    // El rol está en la cuarta parte

                    System.out.println("VERIFICANDO QUE EL EMPLEADO EXISTA Y QUE EL TOKEN SEA VÁLIDO");

                    // Buscar el empleado por username
                    employee = employeeRepository.findByUsername(username).orElse(null);

                    if (employee != null && token != null) {
                        // Verificar que el id y el rol coincidan con los del empleado
                        if (String.valueOf(employee.getId()).equals(id) && employee.getRole().getName().equals(role)) {
                            System.out.println("ROL DEL USUARIO: " + role);

                            // Asignar el rol al contexto de seguridad
                            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(employee.getUsername(), null, authorities);
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                            System.out.println("TOKEN VERIFICADO");
                            System.out.println("USUARIO AUTENTICADO: " + employee.getUsername() + " CON ROL: " + role);
                        } else {
                            System.out.println("EL TOKEN NO COINCIDE CON EL EMPLEADO");
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "EL TOKEN NO ES VÁLIDO");
                            return;
                        }
                    } else {
                        System.out.println("EL EMPLEADO NO EXISTE");
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "EL EMPLEADO NO EXISTE");
                        return;
                    }
                } else {
                    System.out.println("EL TOKEN NO TIENE EL FORMATO CORRECTO");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "EL TOKEN NO ES VÁLIDO");
                    return;
                }
            } else {
                System.out.println("EL USUARIO NO TIENE AUTORIZACIÓN");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "NO TIENES AUTORIZACIÓN");
                return;
            }

        } else {
            System.out.println("EL FILTRO SE EJECUTÓ EN MODO BYPASS");
        }

        filterChain.doFilter(request, response);

        System.out.println("CIERRE DEL FILTRO AUTHFILTER");
    }
}