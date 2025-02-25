Project Overview:
Project Name: 

This is a backend application built using Spring Boot that provides secure authentication and authorization using JWT (JSON Web Tokens). The application supports user registration, login, role-based access control, and token validation, with a focus on scalability, security, and maintainability.

Key Features:
User Registration & Login:

The application allows users to register by providing their username and password. The password is encrypted using BCrypt before saving it to the database.
Upon successful login, a JWT token is generated that contains user role information and is sent back to the client.
JWT Authentication:

JWT is used for authentication instead of traditional session-based authentication, providing a stateless, scalable approach.
Once a user is authenticated, they can access protected routes by sending the JWT token in the request header.
The token is validated with every request using a custom filter (JwtTokenFilter), ensuring the token is valid, not expired, and correctly signed.
Role-based Access Control (RBAC):

Users can be assigned roles such as USER or ADMIN. The application uses Spring Security to enforce role-based access control (RBAC).
For example, the /admin/** endpoint is restricted to users with the ADMIN role, ensuring only authorized users can access sensitive data.
Exception Handling:

The application has custom exception handling to provide meaningful error messages to the client, such as when credentials are invalid or a user is not found.
Admin Management:

Admin users can view their own information through the /me endpoint, which returns details of the currently authenticated user.
Tech Stack:
Backend: Spring Boot
Security: Spring Security, JWT for authentication, BCrypt for password hashing
Database: MySQL with JPA for ORM, using entities like User, Role, and Authority
Dependency Injection: Spring’s @Autowired to inject services and repositories
RESTful API: The application exposes endpoints for registration, login, token validation, and user management.
Architecture Overview:
User Entity:

The User entity stores user-specific information like username, password, and roles. A User can have multiple roles through a many-to-many relationship.
Security Configuration:

Spring Security is configured to manage authentication and authorization, using CustomUserDetailsService to load user information and the JwtTokenFilter to validate JWT tokens on each request.
JWT Token Generation:

The JwtService is responsible for generating and validating JWT tokens. Tokens contain user role information (e.g., ROLE_ADMIN), which is used to control access to protected endpoints.
Exception Handling:

The GlobalExceptionHandler ensures that custom error messages are returned for common authentication and authorization errors (like invalid credentials or unauthorized access).
Access Denied Handling:

The CustomAccessDeniedHandler handles cases where a user tries to access a resource they don't have permission for, returning an appropriate error message.
Security Flow (JWT):
User Logs In:

The user sends a POST request with their credentials (username and password) to the /auth/login endpoint.
If the credentials are correct, the server generates a JWT token with user information and roles and sends it back to the client.
Accessing Protected Routes:

For every subsequent request to a protected route, the client includes the JWT token in the Authorization header (as Bearer <token>).
The JwtTokenFilter intercepts the request and validates the token. If valid, the user's authentication is set in the SecurityContext, allowing access to the requested resource.
Role-Based Access:

The system checks the user’s roles and grants or denies access based on configured access rules (e.g., only ADMIN users can access /admin/** endpoints).
Why this is important:
Scalability: Stateless JWT tokens ensure that the application can scale horizontally without worrying about session management.
Security: Passwords are securely stored using hashing, and token validation ensures that only authenticated users can access protected resources.
Maintainability: The use of Spring Boot, JPA, and Spring Security ensures that the application is maintainable, modular, and easy to extend.
Summary:
This project showcases my ability to build a secure, role-based authentication system using Spring Boot, Spring Security, and JWT. It highlights my skills in backend development, security practices, and designing scalable RESTful APIs. The system ensures that only authenticated and authorized users can access certain endpoints, making it both secure and efficient.

