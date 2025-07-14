# Testing Guide

## Overview
Comprehensive unit test cases have been implemented for all layers across microservices using JUnit 5, Mockito, and Spring Boot Test.

## Test Structure

### Test Layers
1. **Repository Layer** - `@DataJpaTest`
2. **Service Layer** - `@ExtendWith(MockitoExtension.class)`
3. **Controller Layer** - `@WebMvcTest`

### Services with Complete Test Coverage

| Service | Repository Tests | Service Tests | Controller Tests |
|---------|------------------|---------------|------------------|
| UserService | ✅ | ✅ | ✅ |
| ProductService | ✅ | ✅ | ✅ |
| OrderService | ✅ | ✅ | ✅ |
| PaymentService | ✅ | ✅ | ✅ |
| ShippingService | ✅ | ✅ | ✅ |
| FavouriteService | ✅ | ✅ | ✅ |

## Test Dependencies

### Maven Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Key Testing Libraries
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **AssertJ**: Fluent assertions
- **Spring Boot Test**: Integration testing
- **TestContainers**: Database testing (optional)

## Test Categories

### Repository Tests (`@DataJpaTest`)
- Tests JPA repositories in isolation
- Uses in-memory H2 database
- Tests custom query methods
- Validates entity relationships

```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void shouldFindUserByUsername() {
        // Test implementation
    }
}
```

### Service Tests (`@ExtendWith(MockitoExtension.class)`)
- Tests business logic in isolation
- Mocks repository dependencies
- Validates exception handling
- Tests all service methods

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void shouldCreateUser() {
        // Test implementation
    }
}
```

### Controller Tests (`@WebMvcTest`)
- Tests REST endpoints
- Mocks service layer
- Validates HTTP responses
- Tests request/response mapping

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void shouldGetAllUsers() throws Exception {
        // Test implementation
    }
}
```

## Running Tests

### Run All Tests
```bash
# Run tests for all services
mvn test

# Run tests for specific service
cd UserService
mvn test
```

### Run Specific Test Categories
```bash
# Repository tests only
mvn test -Dtest="*RepositoryTest"

# Service tests only
mvn test -Dtest="*ServiceTest"

# Controller tests only
mvn test -Dtest="*ControllerTest"
```

### Generate Test Reports
```bash
# Generate Surefire reports
mvn surefire-report:report

# Generate JaCoCo coverage report
mvn jacoco:report
```

## Test Patterns

### Given-When-Then Structure
All tests follow the Given-When-Then pattern:

```java
@Test
void shouldCreateUser() {
    // Given - Setup test data
    UserDTO userDTO = UserDTO.builder()
        .firstName("John")
        .email("john@example.com")
        .build();
    
    // When - Execute the method under test
    UserDTO result = userService.save(userDTO);
    
    // Then - Verify the results
    assertThat(result.getUserId()).isNotNull();
    assertThat(result.getFirstName()).isEqualTo("John");
}
```

### Mock Configuration
```java
// Mock repository methods
when(userRepository.save(any(User.class))).thenReturn(savedUser);
when(userRepository.findById(1)).thenReturn(Optional.of(user));

// Verify method calls
verify(userRepository).save(user);
verify(userRepository, times(1)).findById(1);
```

### Exception Testing
```java
@Test
void shouldThrowExceptionWhenUserNotFound() {
    // Given
    when(userRepository.findById(1)).thenReturn(Optional.empty());
    
    // When & Then
    assertThatThrownBy(() -> userService.findById(1))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage("User not found with id: 1");
}
```

## Test Configuration

### Application Properties for Tests
```properties
# src/test/resources/application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.org.springframework.web=DEBUG
```

### Test Profiles
```java
@ActiveProfiles("test")
@SpringBootTest
class IntegrationTest {
    // Integration test implementation
}
```

## Best Practices

### Test Naming
- Use descriptive test method names
- Follow `should{ExpectedBehavior}When{StateUnderTest}` pattern
- Example: `shouldThrowExceptionWhenUserNotFound`

### Test Data
- Use builders for test data creation
- Keep test data minimal and focused
- Use meaningful test values

### Assertions
- Use AssertJ for fluent assertions
- Test both positive and negative scenarios
- Verify all important aspects of the result

### Mocking
- Mock external dependencies only
- Use `@MockBean` for Spring context tests
- Use `@Mock` for unit tests

## Coverage Goals

### Target Coverage
- **Line Coverage**: > 80%
- **Branch Coverage**: > 70%
- **Method Coverage**: > 90%

### Coverage Reports
Access coverage reports at:
- `target/site/jacoco/index.html`
- `target/surefire-reports/`

## Continuous Integration

### GitHub Actions
```yaml
- name: Run Tests
  run: mvn test
  
- name: Generate Coverage Report
  run: mvn jacoco:report
  
- name: Upload Coverage
  uses: codecov/codecov-action@v1
```

### Quality Gates
- All tests must pass before merge
- Coverage thresholds must be met
- No critical SonarQube issues

## Troubleshooting

### Common Issues
1. **Test Database Issues**: Ensure H2 dependency is in test scope
2. **Mock Configuration**: Verify mock setup matches actual method calls
3. **Spring Context**: Use appropriate test annotations for context loading

### Debug Tests
```bash
# Run tests in debug mode
mvn test -Dmaven.surefire.debug

# Run specific test with verbose output
mvn test -Dtest=UserServiceTest -X
```