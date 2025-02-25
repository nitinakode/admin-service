# Backend Application with Secure Authentication and Authorization Using JWT

This is a backend application built using **Spring Boot** that provides secure **authentication** and **authorization** using **JWT (JSON Web Tokens)**. The application supports **user registration**, **login**, **role-based access control**, and **token validation**, with a focus on **scalability**, **security**, and **maintainability**.

## Key Features

### User Registration & Login
- **User Registration**: Users can register by providing a **username** and **password**. The password is securely hashed using **BCrypt** before being saved to the database.
- **User Login**: Upon successful login, the system generates a **JWT token** that contains user-specific information such as **username** and **roles**. This token is sent back to the client and is used for subsequent authentication.
  
### JWT Authentication
- **Stateless Authentication**: The application utilizes **JWT** for authentication instead of traditional session-based approaches. JWT provides a **stateless** authentication mechanism, meaning that the server does not store session information between requests.
- **Token Validation**: Every request to a protected route includes the **JWT token** in the **Authorization header** as `Bearer <token>`. The token is validated using a custom filter (`JwtTokenFilter`), which checks that the token is:
  - Not expired.
  - Correctly signed.
  - Authentic.

### Role-Based Access Control (RBAC)
- **Roles**: Users are assigned roles, such as **USER** or **ADMIN**. This enables the implementation of **role-based access control** (RBAC) where access to certain endpoints is restricted based on user roles.
- **Endpoint Protection**: For instance, the `/admin/**` route is restricted to users with the **ADMIN** role, ensuring only authorized users can access sensitive data.

### Exception Handling
- **Custom Error Messages**: The application provides **meaningful error messages** in cases like invalid credentials, unauthorized access, or when a user is not found. This improves the user experience by providing clear feedback on issues.

### Admin Management
- **Admin Access**: Admin users can view their own information through the `/me` endpoint. This endpoint returns details of the currently authenticated user, such as their username and roles.

---

## Tech Stack

- **Backend**: Spring Boot
- **Security**: Spring Security, JWT for authentication, BCrypt for password hashing
- **Database**: MySQL with JPA for ORM (entities: User, Role, Authority)
- **Dependency Injection**: Spring’s `@Autowired` for injecting services and repositories
- **RESTful API**: Exposes endpoints for registration, login, token validation, user management
- **JWT**: For token-based authentication and authorization

---

## Architecture Overview

### User Entity
- The **User** entity stores user-specific information like **username**, **password**, and **roles**. Users can have **multiple roles** through a many-to-many relationship, which allows for easy role management.

### Security Configuration
- **Spring Security** is configured to manage authentication and authorization in the application. It utilizes:
  - **CustomUserDetailsService** to load user details.
  - **JwtTokenFilter** to validate JWT tokens on each request and set the user context for subsequent operations.

### JWT Token Generation
- The **JwtService** handles token generation and validation. Tokens contain user information (such as **roles**), and are signed using a **secret key**.
  - Tokens are **issued** with a defined **expiration** time.
  - A custom exception handler ensures that invalid or expired tokens return appropriate error messages.

### Exception Handling
- **GlobalExceptionHandler** is used to catch common exceptions such as:
  - Invalid credentials
  - Unauthorized access attempts
  - Resource not found
  
- This helps to ensure that all API responses are consistent and provide actionable feedback to clients.

### Access Denied Handling
- **CustomAccessDeniedHandler** is configured to handle cases where a user tries to access a restricted resource without sufficient permissions, returning a clear message (e.g., `403 Forbidden`).

---

## Security Flow (JWT)

### 1. User Logs In
- The user sends a `POST` request to the `/auth/login` endpoint with their credentials (username and password).
- If the credentials are correct, the server generates a **JWT token** containing the user's information, including roles (e.g., `ROLE_USER`, `ROLE_ADMIN`), and sends it back to the client.

### 2. Accessing Protected Routes
- For each subsequent request to a protected route, the client includes the **JWT token** in the `Authorization` header as `Bearer <token>`.
- The `JwtTokenFilter` intercepts the request, validates the token, and if valid, sets the **Authentication object** in the `SecurityContext`. This allows access to the protected resource.

### 3. Role-Based Access
- The system checks the user’s **roles** and grants or denies access to resources based on the roles specified in the JWT token. For example, the `/admin/**` endpoint is restricted to users with the `ROLE_ADMIN`.

---

## Why This is Important

### Scalability
- **Stateless JWT tokens** ensure that the application can scale horizontally without the need for session management or maintaining server-side state.
- Since tokens are self-contained, they do not require the server to store user session data, making it easy to distribute the load across multiple servers.

### Security
- **Password Security**: User passwords are securely hashed using **BCrypt**, making it impossible to retrieve the original password even if the database is compromised.
- **Token Integrity**: Tokens are signed using a **secret key**, ensuring that any modifications to the token will invalidate it.
- **Token Expiry**: The system enforces token expiration, preventing the use of outdated tokens. A refresh token mechanism can be implemented to allow seamless token renewal without requiring a full re-login.

### Maintainability
- The use of **Spring Boot**, **Spring Security**, and **JPA** ensures that the application is **modular**, **extensible**, and easy to maintain.
- Custom exception handling and the use of common design patterns (like JWT token generation and validation) help in keeping the codebase clean and reusable.

---

## Summary

This project demonstrates my ability to build a secure, scalable backend system using **Spring Boot** and **JWT**. By leveraging **Spring Security**, **JWT tokens**, and **role-based access control (RBAC)**, I have built a system that ensures:
- Only **authenticated** and **authorized** users can access protected endpoints.
- User roles and permissions are used to control access to different parts of the application.
- **Scalability** is ensured through stateless JWT authentication, allowing easy scaling of the application.

This system highlights my skills in backend development, secure authentication, and designing scalable **RESTful APIs**.

---

## Future Improvements

- **Refresh Token Support**: Implement a refresh token mechanism to allow users to obtain a new JWT token without requiring a full login.
- **Two-Factor Authentication (2FA)**: Add support for two-factor authentication to enhance security for sensitive endpoints.
- **Rate Limiting**: Implement rate limiting on API requests to prevent abuse or excessive traffic on certain endpoints.

---

## Setup and Installation

### Prerequisites
- **JDK 11 or later**
- **Maven**
- **MySQL**

### Steps

1. **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd <project-directory>
    ```

2. **Set up the database**:
   - Create a **MySQL database** and update the database connection properties in `application.properties`.

3. **Build the application**:
    ```bash
    mvn clean install
    ```

4. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

5. **Test the API**: 
    Use **Postman** or **cURL** to test the registration, login, and protected routes by passing the JWT token in the Authorization header.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

