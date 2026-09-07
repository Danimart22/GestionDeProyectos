package com.gestionproyectos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
//Extends OncePerRequestFilter hace que este filtro se ejecute solo una vez por cada petición http
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException{
        //Para enviar el token JWT se hace por medio del header HTTP
        String authHeader = request.getHeader("Authorization");

        //Si no hay header no se rechaza la petición por si la ruta no pide autenticación
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        //Se saltan los 7 primeros caracteres porque son el "Bearer "
        String token = authHeader.substring(7);
        String email = jwtService.extraerEmail(token);

        //Se verifica que la petición no este autenticada ya por otra parte para no volver a hacer el trabajo de verificar
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Se busca al usuario real en la base de datos a partir del email extraido del token
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            //Se valida si la firma es correcta y si no ha expirado
            if(jwtService.esTokenValido(token, email)){
                //Autentica al usuario para el resto del framework
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request, response);
    }
}
