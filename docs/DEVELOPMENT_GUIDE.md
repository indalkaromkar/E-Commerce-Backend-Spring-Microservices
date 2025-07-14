# Development Guide

## Development Environment Setup

### IDE Configuration
- **IntelliJ IDEA**: Recommended IDE with Spring Boot plugin
- **VS Code**: Alternative with Spring Boot Extension Pack
- **Eclipse**: With Spring Tools Suite (STS)

### Required Plugins/Extensions
- Lombok plugin
- Spring Boot Tools
- Maven integration
- Git integration
- Database tools

### Code Style Configuration
```xml
<!-- .editorconfig -->
root = true

[*]
charset = utf-8
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true

[*.java]
indent_style = space
indent_size = 4

[*.{yml,yaml}]
indent_style = space
indent_size = 2
```

## Project Structure

### Standard Maven Structure
```
service-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gfg/servicename/
│   │   │       ├── ServiceNameApplication.java
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/
│   │       └── static/
│   └── test/
│       └── java/
├── pom.xml
└── Dockerfile
```

### Package Organization
- `config/`: Configuration classes
- `controller/`: REST controllers
- `service/`: Business logic
- `repository/`: Data access layer
- `entity/`: JPA entities
- `dto/`: Data transfer objects
- `exception/`: Custom exceptions
- `util/`: Utility classes

## Coding Standards

### Naming Conventions
```java
// Classes: PascalCase
public class UserService {}

// Methods and variables: camelCase
public void createUser() {}
private String userName;

// Constants: UPPER_SNAKE_CASE
public static final String DEFAULT_ROLE = "USER";

// Packages: lowercase
package com.gfg.userservice.controller;
```

### Entity Design
```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

### DTO Pattern
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

### Controller Design
```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<PagedResponse<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        PagedResponse<UserDto> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
```

### Service Layer
```java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public PagedResponse<UserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        
        List<UserDto> userDtos = users.getContent()
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
            
        return PagedResponse.<UserDto>builder()
            .content(userDtos)
            .page(page)
            .size(size)
            .totalElements(users.getTotalElements())
            .totalPages(users.getTotalPages())
            .build();
    }
}
```

## Testing Strategy

### Unit Testing
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void shouldCreateUser() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .build();
            
        User user = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .build();
            
        UserDto expectedDto = UserDto.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .build();
        
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedDto);
        
        // When
        UserDto result = userService.createUser(request);
        
        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(userRepository).save(user);
    }
}
```

### Integration Testing
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldCreateUser() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .build();
        
        // When
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
            "/api/users", request, UserDto.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(userRepository.count()).isEqualTo(1);
    }
}
```

### Test Configuration
```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public Clock testClock() {
        return Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);
    }
    
    @Bean
    @Primary
    public JavaMailSender mockMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }
}
```

## Database Management

### Flyway Migrations
```sql
-- V1__Create_users_table.sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
```

### Repository Pattern
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date")
    List<User> findUsersCreatedAfter(@Param("date") LocalDateTime date);
    
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") Long userId, 
                           @Param("loginTime") LocalDateTime loginTime);
}
```

## Configuration Management

### Application Properties
```properties
# Server Configuration
server.port=${PORT:9050}
server.servlet.context-path=/user-service

# Database Configuration
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/ecommerce_userservice}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=${DDL_AUTO:update}
spring.jpa.show-sql=${SHOW_SQL:false}
spring.jpa.properties.hibernate.format_sql=true

# Security Configuration
jwt.secret=${JWT_SECRET:default-secret-key}
jwt.expiration=${JWT_EXPIRATION:3600000}

# Logging Configuration
logging.level.com.gfg=${LOG_LEVEL:INFO}
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

### Configuration Classes
```java
@Configuration
@EnableConfigurationProperties({JwtProperties.class, MailProperties.class})
public class ApplicationConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## Error Handling

### Global Exception Handler
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message(ex.getMessage())
            .build();
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Resource Not Found")
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(error -> FieldError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());
            
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Request validation failed")
            .fieldErrors(fieldErrors)
            .build();
            
        return ResponseEntity.badRequest().body(error);
    }
}
```

### Custom Exceptions
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}

public class BusinessException extends RuntimeException {
    private final String errorCode;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
```

## Security Implementation

### JWT Configuration
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

## Logging and Monitoring

### Structured Logging
```java
@Slf4j
@Service
public class UserService {
    
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        
        try {
            // Business logic
            UserDto user = processUserCreation(request);
            
            log.info("User created successfully with id: {}", user.getId());
            return user;
            
        } catch (Exception e) {
            log.error("Failed to create user with username: {}", 
                     request.getUsername(), e);
            throw e;
        }
    }
}
```

### Custom Metrics
```java
@Component
@RequiredArgsConstructor
public class UserMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter userCreationCounter;
    private final Timer userCreationTimer;
    
    @PostConstruct
    public void init() {
        userCreationCounter = Counter.builder("user.creation.count")
            .description("Number of users created")
            .register(meterRegistry);
            
        userCreationTimer = Timer.builder("user.creation.time")
            .description("Time taken to create user")
            .register(meterRegistry);
    }
    
    public void incrementUserCreation() {
        userCreationCounter.increment();
    }
    
    public Timer.Sample startUserCreationTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Development Workflow

### Git Workflow
```bash
# Feature development
git checkout -b feature/user-management
git add .
git commit -m "feat: add user creation endpoint"
git push origin feature/user-management

# Create pull request
# After review and approval
git checkout main
git pull origin main
git merge feature/user-management
git push origin main
```

### Code Review Checklist
- [ ] Code follows naming conventions
- [ ] Proper error handling implemented
- [ ] Unit tests written and passing
- [ ] Integration tests added where needed
- [ ] Documentation updated
- [ ] Security considerations addressed
- [ ] Performance implications considered
- [ ] Logging added for important operations

### Pre-commit Hooks
```bash
#!/bin/sh
# .git/hooks/pre-commit

# Run tests
mvn test
if [ $? -ne 0 ]; then
    echo "Tests failed. Commit aborted."
    exit 1
fi

# Check code formatting
mvn spotless:check
if [ $? -ne 0 ]; then
    echo "Code formatting issues found. Run 'mvn spotless:apply' to fix."
    exit 1
fi
```