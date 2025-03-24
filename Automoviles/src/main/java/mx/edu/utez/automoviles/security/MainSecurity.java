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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class MainSecurity implements WebMvcConfigurer {

    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private CustomInterceptor customInterceptor;

    private final static String[] WHITE_LIST = {
            "/api/auth/login"
    };

    public static String[] getWHITE_LIST() {
        return WHITE_LIST;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desactiva CSRF
                .cors(cors -> cors.configure(http)) // Habilita CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll() // Rutas públicas
                        .requestMatchers("/api/brands/**").hasRole("ADMIN") // Solo ADMIN
                        .requestMatchers("/api/cars/**").hasRole("ADMIN") // Solo ADMIN
                        .requestMatchers("/api/car_has_services/**").hasRole("ADMIN") // Solo ADMIN
                        .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "EMPLEADO") // ADMIN o EMPLEADO
                        .requestMatchers("/api/employees/**").hasRole("ADMIN") // Solo ADMIN
                        .requestMatchers("/api/roles/**").hasRole("ADMIN") // Solo ADMIN
                        .requestMatchers("/api/services/**").hasRole("ADMIN") // Solo ADMIN
                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                ).addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS en todas las rutas
                .allowedOrigins("http://localhost:5173") // Origen permitido (tu frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Headers permitidos
                .allowCredentials(true); // Permite el envío de credenciales (cookies, tokens)
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor).addPathPatterns("/api/test/**");
    }
}