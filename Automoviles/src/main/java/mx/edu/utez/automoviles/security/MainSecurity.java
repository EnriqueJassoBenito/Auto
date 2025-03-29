package mx.edu.utez.automoviles.security;

import mx.edu.utez.automoviles.security.filters.AuthFilter;
import mx.edu.utez.automoviles.security.interceptors.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class MainSecurity implements WebMvcConfigurer {

    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private CustomInterceptor customInterceptor;

    // Lista blanca de endpoints públicos
    private static final String[] WHITE_LIST = {
            "/api/auth/login",
            "/api/auth/register", // Si tienes registro
            "/swagger-ui/**",     // Documentación Swagger
            "/v3/api-docs/**",    // OpenAPI
            "/error"              // Manejo de errores
    };

    public static String[] getWHITE_LIST() {
        return WHITE_LIST;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        // Rutas de administración (solo ADMIN)
                        .requestMatchers(
                                "/api/brands/**",
                                "/api/cars/**",
                                "/api/car_has_services/**",
                                "/api/employees/**",
                                "/api/roles/**",
                                "/api/services/**"
                        ).hasRole("ADMIN")
                        // Rutas para empleados (ADMIN o EMPLEADO)
                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "EMPLEADO")
                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración CORS centralizada
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",  // Frontend local
                "https://tudominio.com"  // Producción
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L); // Cache de 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Interceptores personalizados (opcional)
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor)
                .addPathPatterns("/api/test/**");
    }
}