package com.gestionproyectos.exception;

// @ControllerAdvice intercepta excepciones lanzadas desde CUALQUIER controlador de la aplicación,
// centralizando el manejo de errores en un solo lugar, en vez de repetir try/catch en cada endpoint
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice = @ControllerAdvice + @ResponseBody, para que las respuestas
// de error también se conviertan automáticamente a JSON, igual que los controladores normales

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Este método captura específicamente BadCredentialsException y UsernameNotFoundException,
    // que son las excepciones que Spring Security lanza cuando el login falla
    // (contraseña incorrecta o usuario que no existe)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(Exception ex){
        // Construimos un cuerpo de respuesta JSON consistente para todos los errores de esta API
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status" , HttpStatus.UNAUTHORIZED.value()); // 401
        body.put("error", "Credenciales invalidas");
        // No se revela SI fue el email o la contraseña lo que falló - por seguridad,
        // revelar cuál de los dos es incorrecto facilita ataques de enumeración de usuarios
        body.put("message", "El email o la contraseña con incorrectos");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // Captura los errores de validación de Bean Validation (@NotBlank, @Email, @Size, etc.)
    // que declaramos en RegistroRequest y LoginRequest
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeValidacion(MethodArgumentNotValidException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        // Recopilamos TODOS los errores de validación (puede haber varios campos inválidos
        // a la vez, por ejemplo email mal formado Y contraseña muy corta al mismo tiempo)
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            // Convertimos cada error a un par "nombreDelCampo" -> "mensaje de error"
            // usando los mensajes personalizados que definimos en las anotaciones (@NotBlank(message = "..."))
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        body.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    // Captura cualquier otra excepción no manejada explícitamente arriba -
    // es una red de seguridad para que NUNCA se filtre un stack trace crudo al cliente,
    // sin importar qué error inesperado ocurra en el servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGenerico(Exception ex){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());// 500
        body.put("error", "Error interno del servidor");
        body.put("message", "Ocurrió un error inesperado. Intenta de nuevo más tarde");
        // Este es el punto donde se registra el error completo
        // en un sistema de logging sin exponer esos detalles internos al cliente que hizo la petición
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
