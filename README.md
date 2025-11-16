# Proyecto
# Sistema de Gestión de Estudiantes - API RESTful

Universidad Panamericana
Facultad de Ingeniería y Ciencias Aplicadas
Ingeniería de Sistemas y Tecnología de la Información y Comunicación
Programación II


## Descripción del Proyecto

API RESTful desarrollada con Spring Boot para la gestión integral de estudiantes. El sistema implementa operaciones CRUD completas (Crear, Leer, Actualizar, Eliminar) con validaciones robustas y arquitectura empresarial de 3 capas.

Este proyecto cumple con todos los requerimientos establecidos en el documento de especificaciones, incluyendo arquitectura de n capas, validaciones específicas, pruebas unitarias con JUnit 5 y Mockito, y manejo de excepciones centralizado.


## Características Principales

- Arquitectura de 3 capas (Controller, Service, Repository)
- 6 endpoints RESTful completamente funcionales
- Validaciones exhaustivas a nivel de modelo y negocio
- Almacenamiento en memoria con estructuras thread-safe
- IDs autoincrementables
- Manejo centralizado de excepciones
- 42 pruebas unitarias automatizadas
- Interfaz web interactiva (bonus)
- Documentación completa del código


## Tecnologías Utilizadas

- Java: 12.0.2
- Spring Boot: 2.7.18
- Maven: Gestión de dependencias
- JUnit 5: Framework de pruebas
- Mockito: Mocking para pruebas unitarias
- Lombok: Reducción de código boilerplate
- Bean Validation (javax): Validación de datos


## Requisitos Previos

Para ejecutar este proyecto necesitas:

- JDK 12 o superior instalado
- Maven 3.6 o superior (opcional, el proyecto incluye Maven Wrapper)
- Navegador web moderno (Chrome, Firefox, Safari, Edge)
- Git (para clonar el repositorio)


## Instalación y Ejecución

### Opción 1: Usando Maven Wrapper (Recomendado)

1. Clonar el repositorio:
git clone https://github.com/TU-USUARIO/student-management-api.git
cd student-management-api

2. Compilar el proyecto:
./mvnw clean install

3. Ejecutar la aplicación:
./mvnw spring-boot:run

4. Acceder a la aplicación:
Interfaz web: http://localhost:8080/
API REST: http://localhost:8080/api/students


### Opción 2: Usando Maven instalado localmente

1. Clonar el repositorio
2. mvn clean install
3. mvn spring-boot:run
4. Acceder a http://localhost:8080/


### Opción 3: Ejecutar el JAR directamente

1. Compilar: ./mvnw clean package
2. Ejecutar: java -jar target/student-management-api-1.0.0.jar
3. Acceder a http://localhost:8080/

## Modelo de Datos

Cada estudiante en el sistema tiene los siguientes atributos:

Campo: id
Tipo: Long
Descripción: Identificador único autoincrementable
Obligatorio: Asignado automáticamente por el sistema

Campo: nombre
Tipo: String
Descripción: Nombre completo del estudiante
Obligatorio: Sí
Validación: Máximo 255 caracteres
Ejemplo: "Roberto Antonio Medrano Rivera"

Campo: correo
Tipo: String
Descripción: Correo electrónico del estudiante
Obligatorio: Sí
Validación: Formato de email válido, único en el sistema
Ejemplo: "roberto@example.com"

Campo: numero_telefono
Tipo: String
Descripción: Número de teléfono del estudiante
Obligatorio: Sí
Validación: Exactamente 8 dígitos numéricos
Ejemplo: "12345678"

Campo: idioma
Tipo: String
Descripción: Idioma principal del estudiante
Obligatorio: Sí
Validación: Solo se permiten: "español", "inglés", "francés"
Ejemplo: "español"

## Pruebas Unitarias

El proyecto incluye 42 casos de prueba automatizados distribuidos en 3 suites:

StudentRepositoryTest.java (15 pruebas)
Verifica todas las operaciones del repositorio:
- Guardar estudiantes con ID autoincremental
- Búsqueda por ID y correo (case-insensitive)
- Actualización y eliminación de registros
- Validación de correos duplicados
- Generación secuencial de IDs
- Limpieza de datos

StudentServiceTest.java (13 pruebas)
Verifica la lógica de negocio con Mockito:
- Obtención de estudiantes (todos y por ID)
- Creación con validación de duplicados
- Actualización completa y parcial
- Eliminación con verificación de existencia
- Manejo de excepciones de negocio
- Conversión entre DTO y Entity

StudentControllerTest.java (14 pruebas)
Verifica los endpoints HTTP con MockMvc:
- Respuestas HTTP correctas (200, 201, 404, 409)
- Validación de JSON de entrada
- Manejo de errores de validación
- Verificación de campos obligatorios
- Validación de formatos (email, teléfono, idioma)

Ejecutar todas las pruebas:
./mvnw test

Resultado esperado:
Tests run: 42, Failures: 0, Errors: 0, Skipped: 0


## Arquitectura del Sistema

El proyecto implementa una arquitectura de 3 capas claramente definidas:

Capa de Presentación (Controller)
Responsabilidades:
- Recibir y procesar peticiones HTTP
- Validar datos de entrada con @Valid
- Serializar/deserializar JSON
- Retornar códigos de estado HTTP apropiados
- Delegar lógica de negocio al Service

Clase principal: StudentController

Capa de Negocio (Service)
Responsabilidades:
- Implementar toda la lógica de negocio
- Validar reglas de dominio (correos únicos)
- Convertir entre DTO y Entity
- Coordinar operaciones entre capas
- Lanzar excepciones de negocio

Clase principal: StudentService

Capa de Datos (Repository)
Responsabilidades:
- Gestionar el almacenamiento de datos
- Implementar operaciones CRUD
- Garantizar integridad de datos
- Generar IDs autoincrementables
- Proporcionar operaciones de búsqueda

Clase principal: StudentRepository

Flujo de una petición típica:
Cliente → Controller → Service → Repository → Service → Controller → Cliente


## Manejo de Excepciones

El sistema implementa un manejo centralizado de excepciones con GlobalExceptionHandler:

Excepciones personalizadas:

ResourceNotFoundException
Código HTTP: 404 Not Found
Uso: Cuando un estudiante solicitado no existe
Ejemplo: GET /api/students/999

DuplicateResourceException
Código HTTP: 409 Conflict
Uso: Cuando se intenta crear/actualizar con un correo que ya existe
Ejemplo: POST con correo duplicado

InvalidDataException
Código HTTP: 400 Bad Request
Uso: Cuando los datos proporcionados son inválidos
Ejemplo: Campos con formato incorrecto

MethodArgumentNotValidException
Código HTTP: 400 Bad Request
Uso: Errores de validación de Bean Validation
Ejemplo: Nombre vacío, email inválido, teléfono con menos de 8 dígitos

Todas las excepciones retornan un objeto ErrorResponse estructurado:
{
  "status": 404,
  "mensaje": "Estudiante con ID 5 no encontrado",
  "timestamp": "2024-11-16T20:30:00"
}


## Interfaz Web

El proyecto incluye una interfaz web moderna y responsiva accesible en:
http://localhost:8080/

Características de la interfaz:
- Formulario intuitivo para crear/editar estudiantes
- Validación en tiempo real de campos
- Lista visual de todos los estudiantes en tarjetas
- Botones de acción (Editar, Eliminar)
- Mensajes de confirmación de operaciones
- Diseño responsive (funciona en móviles y tablets)
- Actualización dinámica sin recargar página
- Diálogos de confirmación para acciones destructivas

Tecnologías del Front-End:
- HTML5
- CSS3 (con diseño moderno y gradientes)
- JavaScript Vanilla (sin frameworks adicionales)
- Fetch API para comunicación con el back-end

## Limitaciones Conocidas

- El almacenamiento es en memoria: Los datos se pierden al reiniciar la aplicación
- No hay autenticación ni autorización implementada
- No hay paginación en el endpoint GET /api/students
- El sistema solo soporta un idioma por estudiante
- No hay logs persistentes en archivo
- No hay rate limiting en los endpoints

## Cumplimiento de Requisitos

Requisito del Documento: Arquitectura de 3 capas
Estado: CUMPLIDO
Evidencia: Controller, Service, Repository claramente separados

Requisito del Documento: JavaEE con Spring Boot
Estado: CUMPLIDO
Evidencia: Spring Boot 2.7.18, Java 12

Requisito del Documento: JDK 11 o 17
Estado: CUMPLIDO
Evidencia: Usa JDK 12 

Requisito del Documento: Operaciones CRUD completas
Estado: CUMPLIDO
Evidencia: 6 endpoints implementados (GET, POST, PUT, PATCH, DELETE)

Requisito del Documento: Validaciones específicas
Estado: CUMPLIDO
Evidencia: Bean Validation + validaciones de negocio

Requisito del Documento: Pruebas unitarias con JUnit 5 y Mockito
Estado: CUMPLIDO
Evidencia: 42 pruebas automatizadas

Requisito del Documento: Diagrama de clases
Estado: CUMPLIDO
Evidencia: Diagrama completo en documento word entregable

Requisito del Documento: Despliegue en GitHub
Estado: CUMPLIDO
Evidencia: Repositorio público en GitHub

## Datos

Autor
Nombre: Roberto Antonio Medrano Rivera
Correo: medranor.roberto@gmail.com
Institución: Universidad Panamericana
Facultad: Ingeniería y Ciencias Aplicadas
Carrera: Ingeniería de Sistemas y TIC
Curso: Programación II
Fecha: Noviembre 2025


Licencia
Este proyecto es de uso académico para la Universidad Panamericana.
