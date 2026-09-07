package com.gestionproyectos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

//Service vuelve la clase un componente gestionado por Spring (bean), para que se pueda inyectar donde sea
//simplemente declarandola como dependencia en el constructor sin tener que instanciarla con new
@Service
public class JwtService {
    private final SecretKey clave;
    private final long expiracionMS;

    public JwtService(
            //Se inyecta los valores de application.yml con Spring directamente en el constructor
             @Value("${app.jwt.secret}") String secret,
             @Value ("${app.jwt.expiration-ms}") long expiracionMS
    ){
        //Convierte el String de la clase secreta en un objeto de tipo SecretKey para que la libreria JJWT
        //pueda usarlo para firmar y verificar
        this.clave = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiracionMS = expiracionMS;
    }
    //Este metodo construye el token en 3 partes, subject(email) que determina el dueño del token
    //issuedAt y expiration determinan las fechas de emisión del token y de expiración
    //singWith hace que el token se firme con la clave secreta que definimos
    public String generarToken(String email){
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expiracionMS);

        return Jwts.builder().subject(email).issuedAt(ahora).expiration(expiracion).signWith(clave).compact();

    }
    //Estos 3 metodos verifican que la firma sea valida, sino salta error
    //al igual que verifican que no haya expirado
    public String extraerEmail(String token){
        return extraerClaim(token, Claims::getSubject);
    }

    public boolean esTokenValido(String token, String email){
        String emailDelToken = extraerEmail(token);
        return emailDelToken.equals(email) && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token){
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }
    //Este metodo generico funciona para extraer el email, fecha de expiración etc.
    private <T> T extraerClaim(String token, Function<Claims, T> resolver){
        Claims claims = Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }
}
