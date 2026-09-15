package com.gestionproyectos.config;

import com.gestionproyectos.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
//Activa la configuración personalizada de Spring Security y desactiva el comportamiento por defento
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    //Este Bean hashea las contraseñas al registrar usuarios y para comparar las contraseñas al hacer login
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    //Este es el componente central de spring Security, maneja el proceso de autenticación
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //CSRF (Cross-Site Request Forgery) es una protección relevante para aplicaciones que usan sesiones con cookies. Como nuestra API es stateless (sin sesiones, cada petición se autentica
        // independientemente con su propio JWT), esta protección no aplica aquí — de hecho, dejarla activa causaría problemas innecesarios con peticiones POST/PUT/DELETE
        //.sessionManagement(... SessionCreationPolicy.STATELESS) — esta es la línea que declara explícitamente: "este servidor nunca va a crear ni mantener una sesión HTTP". Cada petición debe traer su propio JWT y ser autoexplicativa
        // — el servidor no "recuerda" nada entre peticiones. Esto es clave para poder escalar horizontalmente después en AWS (múltiples instancias de el backend detrás de un load balancer, sin necesidad de sincronizar sesiones entre ellas).
        http.csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS) ))
                //.authorizeHttpRequests(...) — aquí se definen las reglas de acceso: "/api/auth/**" con permitAll() — las rutas de registro y login deben ser públicas (¡obviamente! nadie tiene un JWT todavía si no se ha registrado o logueado)
                //.anyRequest().authenticated(), todo lo demás requiere un JWT válido. Esta es la política "seguro por defecto" que queremos: cualquier endpoint nuevo que crees en el futuro va a estar protegido automáticamente,
                // a menos que lo agregues explícitamente a la lista de rutas públicas.
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated()).authenticationProvider(authenticationProvider())
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) — esta es la línea que finalmente activa nuestro filtro JWT: le dice a Spring Security "ejecuta mi
                // filtro personalizado antes del filtro estándar de autenticación por usuario/contraseña, en cada petición".
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
