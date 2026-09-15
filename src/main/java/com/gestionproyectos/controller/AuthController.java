package com.gestionproyectos.controller;


// Importamos los DTOs que acabamos de crear
import com.gestionproyectos.model.dto.AuthResponse;
import com.gestionproyectos.model.dto.LoginRequest;
import com.gestionproyectos.model.dto.RegistroRequest;

// Importamos la entidad de dominio Usuario
import com.gestionproyectos.model.entity.Usuario;

// Importamos el repositorio para guardar/buscar usuarios en la base de datos
import com.gestionproyectos.repository.UsuarioRepository;

// Importamos nuestro servicio de JWT (generar tokens)
import com.gestionproyectos.security.JwtService;

// Anotación para validar automáticamente el body de la petición usando las reglas @NotBlank, @Email, etc.
import jakarta.validation.Valid;

// Clases de Spring para construir respuestas HTTP con código de estado explícito
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// El componente central de Spring Security que valida credenciales usuario/contraseña
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Interfaz para hashear y verificar contraseñas (implementada con BCrypt en SecurityConfig)
import org.springframework.security.crypto.password.PasswordEncoder;

// Anotaciones para exponer endpoints REST
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController: combina @Controller + @ResponseBody - le dice a Spring que esta clase
// expone endpoints HTTP y que los valores que retornan los métodos se convierten
// automáticamente a JSON en la respuesta (no a vistas HTML)
@RestController
// @RequestMapping: todos los endpoints de esta clase empiezan con este prefijo de URL
@RequestMapping("/api/auth")
public class AuthController {
    // Dependencias que necesitamos, todas inyectadas por el constructor (nunca con @Autowired en el campo,
    // eso es considerado mala práctica moderna - la inyección por constructor permite que los campos sean
    // 'final' e inmutables, y hace explícito qué necesita la clase para funcionar)
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructor único: Spring detecta automáticamente que debe inyectar estos 4 beans aquí
    // (no hace falta @Autowired explícito cuando la clase tiene un solo constructor)
    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // @PostMapping("/registro") -> este método responde a POST /api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(
            // @Valid activa las validaciones (@NotBlank, @Email, @Size) declaradas en RegistroRequest
            // @RequestBody le dice a Spring que convierta el JSON del body de la petición a este objeto
            @Valid @RequestBody RegistroRequest request
    ) {
        // Verificamos que no exista ya un usuario con ese email (usando el método
        // existsByEmail que declaramos en UsuarioRepository )
        if (usuarioRepository.existsByEmail(request.email())) {
            // Si ya existe, respondemos 409 Conflict (código HTTP estándar para "ya existe un recurso así")
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        // Hasheamos la contraseña en texto plano que llegó en el request -
        // NUNCA guardamos la contraseña original, solo su hash irreversible
        String passwordHash = passwordEncoder.encode(request.password());
        // Creamos la entidad Usuario usando el constructor de negocio que definimos
        // (el que garantiza que un Usuario nace siempre con todos sus campos obligatorios)
        Usuario nuevoUsuario = new Usuario(request.email(), passwordHash, request.nombre());
        // Guardamos el usuario en la base de datos - save() viene gratis de JpaRepository
        usuarioRepository.save(nuevoUsuario);

        // Generamos un token JWT inmediatamente, para que el usuario quede logueado
        // automáticamente después de registrarse (evita un paso extra de login manual)
        String token = jwtService.generarToken(nuevoUsuario.getEmail());

        // Construimos la respuesta con el token y los datos básicos del usuario
        AuthResponse response = new AuthResponse(token, nuevoUsuario.getEmail(), nuevoUsuario.getNombre());

        // 201 Created es el código HTTP correcto cuando se crea un nuevo recurso exitosamente
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Este método responde a POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        // authenticationManager.authenticate(...) es el punto donde Spring Security
        // realmente verifica las credenciales: internamente usa el DaoAuthenticationProvider
        // que configuramos (que a su vez usa CustomUserDetailsService + PasswordEncoder)
        // Si el email no existe o la contraseña no coincide, este método lanza una excepción
        // automáticamente (AuthenticationException) - no necesitamos verificar nosotros mismos
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        // Si llegamos a esta línea, las credenciales fueron válidas (si no, ya se habría lanzado
        // una excepción arriba y este código nunca se ejecutaría)
        // Buscamos el usuario completo para obtener su nombre (el AuthenticationManager
        // solo confirma que las credenciales son correctas, no nos da la entidad completa)
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(); // no debería pasar nunca aquí, porque ya se autenticó exitosamente
        // Generamos un nuevo token para esta sesión de login
        String token = jwtService.generarToken(usuario.getEmail());

        AuthResponse response = new AuthResponse(token, usuario.getEmail(), usuario.getNombre());

        // 200 OK es el código correcto para un login exitoso (no estamos creando nada nuevo,
        // a diferencia del registro que sí crea un recurso -> 201)
        return ResponseEntity.ok(response);
    }
}
