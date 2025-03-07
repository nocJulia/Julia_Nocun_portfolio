# Configuration
### PostgreSQL Configuration in `docker-compose.yml` file

Below is the PostgreSQL configuration that you can include in your `docker-compose.yml` file:

```yaml
services:
  postgres-dev:
    image: postgres:17
    container_name: postgres-db-dev
    restart: always
    ports:
      - "5433:5432"
    environment:
      POSTGRES_USER: "admin"
      POSTGRES_PASSWORD: "admin"
      POSTGRES_DB: "coordination-system-dev"
    volumes:
      - postgres_data_dev:/var/lib/postgresql/data

volumes:
  postgres_data_dev:
```

#### Configuration Explanation
- **`image`**: Specifies the Docker image for PostgreSQL version 17.
- **`container_name`**: Sets the container name to `postgres-db-dev`.
- **`restart`**: The container will automatically restart if it stops.
- **`ports`**: Maps the external port (`5433`) to the PostgreSQL internal port (`5432`) within the container.
- **`environment`**:
    - `POSTGRES_USER`: Admin username for the database (`admin`).
    - `POSTGRES_PASSWORD`: Admin password for the database (`admin`).
    - `POSTGRES_DB`: Default database name (`coordination-system-dev`).
- **`volumes`**: PostgreSQL data is stored in the `postgres_data_dev` volume, making it persistent across container restarts.

#### How to Use
1. Copy the configuration above into your `docker-compose.yml` file.
2. Run the following command to start the container:

   ```bash
   docker-compose up -d
   ```

### Setting the `dev` Profile in Spring Boot Application

To configure IntelliJ IDEA to use the `dev` profile in your Spring Boot application, you need to modify the **current run configuration**. Here's how to do it:

1. **Open IntelliJ IDEA** and navigate to the **Run/Debug Configurations**:
    - Go to the top-right corner and click on the **Run/Debug Configurations** dropdown.
    - Select **Edit Configurations**.

2. **Select the Spring Boot Application Configuration**:
    - In the **Run/Debug Configurations** window, find the existing configuration for your Spring Boot application.
    - Select the configuration you want to modify.

3. **Edit Active Profiles**:
    - In the selected configuration, find the **Active Profiles** field.
    - Enter `dev` in the **Active Profiles** field to specify that the `dev` profile should be used when running the application.

4. **Save and Run**:
    - Click **OK** to save the changes.
    - Now, when you run your Spring Boot application, it will use the `dev` profile, and the application will connect to the `postgres-dev` database as defined in the Docker configuration.

### Project Structure and Git Workflow

#### Project Structure

Create a folder for your group’s module inside the `coordinationsystem` directory. Within this folder, organize your code according to functionality by creating the following package structure:

```
coordinationsystem/
    └── <your-module-name>/
        ├── controllers/      # Contains the controller classes
        ├── repositories/     # Contains the repository interfaces
        └── services/         # Contains the service classes
```

For example, if your module is called `user-management`, the structure would look like this:

```
coordinationsystem/
    └── user-management/
        ├── controllers/      
        ├── repositories/
        └── services/
```

---

### Git Workflow

1. **Creating a Git Branch**:
    - Each group or individual developer should create a new branch with the name of their module. For example, if your module is named `user-management`, the branch should be named `user-management`.

   To create and push a new branch to the remote repository, use the following commands:

   ```bash
   git checkout -b user-management    # Create a new branch called 'user-management'
   git push -u origin user-management # Push the branch to the remote repository
   ```

2. **Server-Side Rendering with Thymeleaf and Liquibase**:
    - All team members should familiarize themselves with **server-side rendering (SSR)** and its implementation using **Thymeleaf** and **Liquibase**. SSR allows the server to render the HTML pages and send them to the client, improving SEO and performance in some cases.
    - **Thymeleaf** will be used for rendering HTML templates on the server side.
    - **Liquibase** will be used for managing database migrations in a version-controlled way.

3. **Repository Interfaces**:
    - Repositories should implement the `CrudRepository<T, ID>` interface provided by Spring Data. This interface provides basic CRUD operations such as `save()`, `findById()`, `delete()`, etc. Importantly, **you do not need to implement these methods manually**. The implementation is provided by Spring Data based on the method names following the **naming convention**.

   For example, if you want to find a user by their username, the method could be named `findByUsername`, and Spring Data will automatically generate the implementation behind the scenes.

   Example repository interface:
   ```java
   import org.springframework.data.repository.CrudRepository;
   import com.example.model.User;

   public interface UserRepository extends CrudRepository<User, Long> {
       // No need to implement basic CRUD methods
       // The following method will be automatically implemented by Spring Data
       User findByUsername(String username);
   }
   ```

   In the above example, the method `findByUsername` follows Spring Data's naming convention, and Spring will automatically generate the implementation for this method at runtime.


4. **Transactional Methods**:
    - Methods in the service layer that modify the state of the database (e.g., creating, updating, or deleting records) should be annotated with `@Transactional`. This ensures that these operations are executed within a transaction, maintaining data integrity.

   Example service method:
   ```java
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;

   @Service
   public class UserService {

       private final UserRepository userRepository;

       public UserService(UserRepository userRepository) {
           this.userRepository = userRepository;
       }

       @Transactional
       public void createUser(User user) {
           userRepository.save(user);  // This operation will be executed within a transaction.
       }
   }
   ```

---

### Using Spring Annotations (`@Service`, `@Controller`, `@Componetnt`, `@Bean`, etc.)

In Spring, various annotations are used to define components and manage dependency injection. Below are some of the key annotations and how to use them:

1. **`@Service`**:
    - The `@Service` annotation is used to mark a class as a service component, which typically contains business logic. This class will be automatically registered as a Spring Bean, and it can be injected into other classes (like controllers or other services).

   Example:
   ```java
   @Service
   public class UserService {
       // Business logic for user management
   }
   ```

2. **`@Controller`**:
    - The `@Controller` annotation is used for defining Spring MVC controllers, which are responsible for handling HTTP requests and returning responses. These controllers are typically used to serve views (e.g., using Thymeleaf) or to provide APIs (in the case of REST controllers).

   Example:
   ```java
   @Controller
   public class UserController {
       private final UserService userService;

       @Autowired
       public UserController(UserService userService) {
           this.userService = userService;
       }

       // Define request mappings here
   }
   ```

3. **`@Bean`**:
    - The `@Bean` annotation is used in configuration classes to define Spring beans manually. It can be used to provide custom configurations that Spring may not automatically detect through component scanning.

   Example:
   ```java
   @Configuration
   public class AppConfig {
       
       @Bean
       public SomeService someService() {
           return new SomeServiceImpl();
       }
   }
   ```

4. **Dependency injection**:
    - Spring will resolve and inject the required dependency at runtime using constructor.

   Example with constructor injection:
   ```java
   @Service
   public class UserService {
       private final UserRepository userRepository;
       
   //  Automatic dependency injection
       public UserService(UserRepository userRepository) {
           this.userRepository = userRepository;
       }
   }
   ```

---

### Summary of Best Practices

- **Use annotations** like `@Service`, `@Controller`, `@Repository` for defining your Spring Beans, services, controllers, and repositories.
- **Dependency injection** should be handled with auto wiring, typically using constructor injection for better clarity and testability.
- **Transactional methods** should be annotated with `@Transactional` to ensure that database operations are properly managed within a transaction.
- Ensure that each module follows the **package structure** (`repositories`, `services`, `controllers`) inside its own module folder and follows the **Git workflow** for branching and pushing to the remote repository.