# 🎓 Gestor Académico & Sistema de Horarios

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity" alt="Spring Security">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/Spring_Web-6DB33F?style=for-the-badge&logo=spring" alt="Spring Web">
  <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring" alt="Spring Data JPA">
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Thymeleaf-Bootstrap_5-005F00?style=for-the-badge&logo=thymeleaf" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/Lombok-BC2224?style=for-the-badge&logo=apachemaven" alt="Lombok">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven" alt="Maven">
  <img src="https://img.shields.io/badge/API_REST-JSON-000000?style=for-the-badge&logo=json" alt="API REST">
</p>

<p align="center">
  Sistema web integral desarrollado en Java para la gestión académica de perfiles, materias, horarios e inscripciones. Implementa una arquitectura híbrida que combina un monolito tradicional con renderizado en el servidor (SSR) y una <b>API REST Stateless</b> protegida con <b>JWT (JSON Web Tokens)</b> y estructurada mediante DTOs para el consumo de datos de forma externa.
</p>

---

## 🏛️ Arquitectura y Flujo de la Aplicación

El sistema sigue el patrón arquitectónico **MVC (Modelo-Vista-Controlador)** y expone servicios **RESTful** utilizando Spring Boot, manteniendo la capa de servicios pura y trabajando exclusivamente con entidades de dominio:

1. **Controladores Web (`@Controller`):** Gestionados mediante **Spring Web**, reciben las peticiones HTTP del navegador, se comunican con la capa de servicios y devuelven las plantillas HTML.
2. **Controladores REST (`@RestController`):** Proveen una interfaz de comunicación mediante JSON bajo el prefijo `/api/`. Se encargan de recibir las peticiones, mapear los DTOs de entrada hacia Entidades y, tras interactuar con la capa de servicio, mapear las respuestas de vuelta a DTOs para no exponer la lógica de negocio ni la estructura de la base de datos.
3. **Motor de Plantillas (Thymeleaf):** Renderiza los datos dinámicos enviados por el backend directamente en el servidor, inyectando objetos, listas y validaciones en tiempo real con Bootstrap 5.
4. **Persistencia (Spring Data JPA / Hibernate):** Gestiona la comunicación con la base de datos relacional **PostgreSQL** mediante entidades optimizadas con **Lombok** para reducir código repetitivo (Getters, Setters, Constructores).
5. **Seguridad Dual (Spring Security & JWT):** Intercepta todas las peticiones implementando un control de acceso basado en sesiones HTTP para las vistas web, y una arquitectura **Stateless** para la API REST. Un filtro personalizado (`JwtAuthenticationFilter`) valida los tokens Bearer para generar el contexto de autenticación de forma segura y sin guardar estado en el servidor.

---

## 🔌 API REST (Endpoints Disponibles)

El proyecto incluye un paquete dedicado `controller.rest` que agrupa los servicios de la aplicación para su consumo externo. Todos los endpoints privados requieren autenticación mediante el encabezado HTTP `Authorization: Bearer <token>` y utilizan validación estricta mediante `@Valid`.

| Módulo / Controlador | Ruta Base | Operaciones Principales | Descripción |
| :--- | :--- | :--- | :--- |
| **Horarios** | `/api/horario` | `GET`, `POST`, `PATCH`, `DELETE` | Búsqueda general, por ID o por `anioCursada`. Permite guardar, actualizar y eliminar horarios usando `HorarioConMateriaDto`. |
| **Inscripciones** | `/api/inscripcion` | `GET`, `POST`, `PATCH`, `DELETE` | Búsqueda mediante query params (`idPersona`, `anioLectivo`). Crea y actualiza inscripciones vinculando listas de materias (`seleccion`). |
| **Materias** | `/api/materia` | `GET`, `POST`, `PATCH`, `DELETE` | CRUD del catálogo de asignaturas. Búsqueda avanzada combinando `nombre` y `anioCursada` devolviendo listas de `MateriaDto`. |
| **Perfiles** | `/api/perfil` | `GET`, `POST`, `PATCH`, `DELETE` | Gestión de usuarios del sistema. Incluye mapeos diferenciados para creación (`PerfilAGuardarRDto`) y edición (`PerfilAEditarRDto`). |
| **Registro / Auth** | `/api/usuario` | `POST` | Endpoints públicos para el alta de nuevos usuarios en el sistema y la obtención del token JWT de acceso. |

---

## 📂 Descripción de Módulos Web y Vistas

El sistema web está estructurado en 5 grandes módulos funcionales:

* **1. Autenticación y Seguridad:** Control de acceso mediante sesiones HTTP y contraseñas encriptadas. Vistas dedicadas para inicio de sesión y registro de nuevos usuarios con manejo de errores personalizados.
* **2. Gestión de Perfiles:** Módulo CRUD completo orientado al registro, listado y administración de los usuarios del sistema, con buscador rápido por ID.
* **3. Gestión de Materias:** Administración del catálogo de asignaturas disponibles, permitiendo dar de alta nuevas materias, modificar sus datos y consultar la oferta académica.
* **4. Administración de Horarios:** Núcleo del sistema encargado de estructurar y vincular las clases en un cronograma semanal, filtrado por alumno y año lectivo.
* **5. Gestión de Inscripciones:** Módulo transaccional que vincula a los estudiantes con las materias y sus respectivos horarios, contemplando vistas detalladas de alta y modificación.

---

## 📸 Demostración Visual del Sistema Web

> **Acerca del Proyecto:** Recorrido visual por los diferentes módulos de la aplicación web, mostrando la consistencia del diseño en modo oscuro y la interfaz de gestión.

### 🏠 Inicio y Cronograma Principal
<p align="center">
  <img src="assets/home.png" alt="Pantalla de Inicio" width="850">
</p>
<p align="center">
  <img src="assets/vista-horario.png" alt="Vista Principal de Horarios" width="850">
</p>

### ⚙️ Módulos del Sistema (Desplegables)

<details>
  <summary><b>🔐 1. Autenticación y Seguridad</b></summary>
  <br>
  <table align="center">
    <tr>
      <td align="center"><b>Iniciar Sesión</b></td>
      <td align="center"><b>Registro</b></td>
    </tr>
    <tr>
      <td><img src="assets/login.png" width="400"></td>
      <td><img src="assets/registro.png" width="400"></td>
    </tr>
  </table>
</details>

<details>
  <summary><b>👤 2. Gestión de Perfiles (CRUD)</b></summary>
  <br>
  <table align="center">
    <tr>
      <td align="center"><b>Listado General</b></td>
      <td align="center"><b>Buscador por ID</b></td>
    </tr>
    <tr>
      <td><img src="assets/listado-perfiles.png" width="400"></td>
      <td><img src="assets/buscar-perfil-por-id.png" width="400"></td>
    </tr>
    <tr>
      <td align="center"><b>Crear Perfil</b></td>
      <td align="center"><b>Modificar Perfil</b></td>
    </tr>
    <tr>
      <td><img src="assets/crear-perfil.png" width="400"></td>
      <td><img src="assets/modificar-perfil.png" width="400"></td>
    </tr>
  </table>
</details>

<details>
  <summary><b>📚 3. Gestión de Materias</b></summary>
  <br>
  <table align="center">
    <tr>
      <td align="center" colspan="2"><b>Listado de Materias</b></td>
    </tr>
    <tr>
      <td colspan="2" align="center"><img src="assets/listado-materias.png" width="800"></td>
    </tr>
    <tr>
      <td align="center"><b>Crear Materia</b></td>
      <td align="center"><b>Modificar Materia</b></td>
    </tr>
    <tr>
      <td><img src="assets/crear-materia.png" width="400"></td>
      <td><img src="assets/modificar-materia.png" width="400"></td>
    </tr>
  </table>
</details>

<details>
  <summary><b>📅 4. Administración de Horarios</b></summary>
  <br>
  <table align="center">
    <tr>
      <td align="center" colspan="2"><b>Listado de Horarios</b></td>
    </tr>
    <tr>
      <td colspan="2" align="center"><img src="assets/listado-horarios.png" width="800"></td>
    </tr>
    <tr>
      <td align="center"><b>Crear Horario</b></td>
      <td align="center"><b>Modificar Horario</b></td>
    </tr>
    <tr>
      <td><img src="assets/crear-horario.png" width="400"></td>
      <td><img src="assets/modificar-horario.png" width="400"></td>
    </tr>
  </table>
</details>

<details>
  <summary><b>📝 5. Gestión de Inscripciones</b></summary>
  <br>
  <table align="center">
    <tr>
      <td align="center" colspan="2"><b>Listado de Inscripciones</b></td>
    </tr>
    <tr>
      <td colspan="2" align="center"><img src="assets/listado-inscripciones.png" width="800"></td>
    </tr>
    <tr>
      <td align="center"><b>Crear Inscripción</b></td>
      <td align="center"><b>Detalle - Crear</b></td>
    </tr>
    <tr>
      <td><img src="assets/crear-inscripcion.png" width="400"></td>
      <td><img src="assets/mas-detalle-crear-inscripcion.png" width="400"></td>
    </tr>
    <tr>
      <td align="center"><b>Modificar Inscripción</b></td>
      <td align="center"><b>Detalle - Modificar</b></td>
    </tr>
    <tr>
      <td><img src="assets/modificar-inscripciones.png" width="400"></td>
      <td><img src="assets/mas-detalle-modifcar-inscripciones.png" width="400"></td>
    </tr>
  </table>
</details>

---

## 🛠️ Stack Tecnológico

* **Core & Backend:** Java, Spring Boot, Spring Web.
* **Seguridad & Autenticación:** Spring Security, JSON Web Tokens (JJWT), `BCryptPasswordEncoder`.
* **Integración y API:** Patrón DTO, Mappers estáticos, validación de datos (`jakarta.validation`).
* **Base de Datos & Persistencia:** PostgreSQL, Spring Data JPA, Hibernate, Lombok.
* **Frontend / UI:** Thymeleaf, Bootstrap 5 (Dark Mode), Bootstrap Icons.
* **Herramientas de Control y Construcción:** Maven, Git (integrado en VS Code).