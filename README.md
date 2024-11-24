# HR-Management-System

The HR Management System is a desktop application developed using Java, Swing, and JDBC. The system is designed with a three-layer architecture comprising the Data Layer, Business Layer, and Presentation Layer. This modular design ensures that changes in one layer do not impact the others, promoting maintainability and scalability.

Data Layer
Initially, data was managed using file-based storage. Later, the system was upgraded to use MySQL for improved data management and efficiency. The data layer is responsible for storing and managing all application data, utilizing JDBC for seamless interaction with the database.

Business Layer
Acting as an intermediary between the presentation and data layers, the business layer encapsulates the core application logic. It handles data transfer and implements business rules, ensuring a clean separation of concerns and smooth operation of the system.

Presentation Layer
The user interface is developed using Swing, providing an intuitive and interactive user experience. This layer contains all the UI-related code, enabling users to interact with the system effectively.
