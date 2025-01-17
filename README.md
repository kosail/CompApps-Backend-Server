# CompApp Backend - Spring Boot Project
<hr>

This is my **first Spring Boot project**. This backend is designed to support **CompApp**, a mobile application built for college students to request small favors in exchange for compens
The backend connects to the mobile frontend via REST API, allowing the app to perform operations like user registration and login.

Here's how the backendation. The app focuses on three main types of services: **shopping orders**, **educational tasks**, and **rides**.

This repository contains the core functionality of the backend, developed in **Kotlin** using **Spring Boot**. It integrates with the frontend built in **Kotlin Multiplatform Jetpack Compose**.

### Key Features

- **User Service**: Manages user registration, login, and profiles.
- **Modular Structure**: The project follows a clean, modular architecture. The services for handling orders and the repositories are the most "nighly" part of this backend, as they were build when I had no skills in SQL. Nonetheless, the design principles are present throughout the project.

### Integration with Frontend
 integrates with the frontend:
1. **User Authentication**: The frontend app sends user credentials to the backend to authenticate and register users.
2. **Order Management**: The frontend interacts with the backend for managing user orders (creating, modifying, accepting, etc.).
3. **Data Communication**: All data communication between the app and the backend is handled using **JSON** over HTTP, ensuring a smooth integration.

### How to Run the Backend

To run the backend locally:
1. Clone the repository.
2. Make sure you have **JDK 17** or higher installed.
3. Run the application using your IDE or with:
   ```
   ./gradlew bootRun
   ```
4. The API will be available on `http://localhost:8080`. **Consider that this version does not have security in mind and is not proper for production.** It only handles basic verifications within the controllers and services. Also, this repo does not includes the .html files that are sent as email by the EmailService when new users registers or a password change is requested. You have to create them, and configure the application.properties file.

### What's Next?

Since this is my first Spring project, I'm actively working on expanding my skills with:
- **Database Integration**: Future versions will include database access using Spring Data JPA and MariaDB.
- **Full API Functionality**: Extending and securing all API endpoints, including those for order management.
- **Improve security**: Add user authorization and authentication to avoid manual checks or permissions like it is implemented right now.
- **Actuator**: Configure Spring Boot actuator to be able to monitorize the performance of the app from endpoints.

---

### Contact

Feel free to reach out if you have any questions or would like to see more of the project. I'm always open to feedback!

---

### License
This project is licensed under the MIT License.
