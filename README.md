# Sistema de Gestión de Inventario - Prueba Técnica

Este proyecto es una solución básica Full-Stack que hice en 9 horas para la gestión de inventarios, que incluye operaciones CRUD completas y un registro de transacciones. 

## 🛠️ Tecnologías Utilizadas
* **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate.
* **Frontend:** Angular 17, TypeScript, Signals (Manejo de estado).
* **Base de Datos:** PostgreSQL.
* **Infraestructura:** Docker & Docker Compose.

## ⚙️ Cómo ejecutar el proyecto en local

### 1. Configuración del entorno
1. Clona este repositorio.
2. En la raíz del proyecto, crea un archivo llamado `.env` basándote en el archivo `.env.example` proporcionado.
3. Completa el `.env` con tus credenciales locales para PostgreSQL.

### 2. Levantar el Backend y la Base de Datos (Docker)
El proyecto está configurado con Docker Compose para orquestar la base de datos y el backend sin necesidad de configuraciones locales adicionales.
1. Abre una terminal en la raíz del proyecto.
2. Ejecuta el siguiente comando para compilar el `.jar` de Java (requiere Maven):
   `cd gestion-inventario && ./mvnw clean package -DskipTests`
3. Regresa a la raíz y levanta los contenedores:
   `docker-compose up --build`
*(El backend estará disponible en `http://localhost:8080`)*

### 3. Levantar el Frontend (Angular)
1. Abre una nueva terminal en la carpeta `inventario-frontend`.
2. Instala las dependencias:
   `npm install`
3. Inicia el servidor de desarrollo:
   `ng serve`
4. Abre tu navegador en `http://localhost:4200`.
