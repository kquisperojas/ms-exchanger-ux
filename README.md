# 🎯 API de Experiencia - ms-exchanger-ux

Este proyecto implementa el **API de Experiencia** que se comunica con el cliente (front-end), valida usuarios en el servicio externo **GoRest**, y consume el **API de Soporte (ms-exchanger-sp)** para registrar operaciones de tipo de cambio.

---

## 🚀 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring WebFlux** (programación reactiva)
- **Spring Security** (autenticación con Basic Auth o JWT)
- **WebClient** (consumo de APIs externas)
- **Maven** (gestión de dependencias y build)
- **Lombok** (reducción de código boilerplate)
- **Postman** (para pruebas de endpoints)

---
## ⚙️ Configuración (`application.yml`)

```yaml
server:
  port: 8081

spring:
  application:
    name: ms-exchanger-ux

security:
  basic:
    enabled: true

api:
  gorest:
    base-url: https://gorest.co.in/public/v2
  soporte:
    base-url: http://localhost:8080/api/v1/soporte

```
```
🔑 Endpoints disponibles
1. Realizar tipo de cambio  
POST /api/v1/experience/exchange
Valida usuario en GoRest.
Calcula monto final.
Llama al API de Soporte para registrar en BD.
Devuelve mensaje de confirmación.

```
🧪 Ejemplo en Postman
Realizar cambio
POST http://localhost:8081/api/v1/experiencia/registrarCambio
```
json
{
  "id": 8548989,
  "userName": "Kevin",
  "montoInicial": 200.0,
  "tipoCambio": 3.8,
  "monedaOrigen": "USD",
  "monedaDestino": "PEN"
}


```
▶️ Ejecución del proyecto
Clonar el repositorio:
git clone https://github.com/kquisperojas/ms-exchanger-ux.git

```
Acceder a la carpeta:
cd ms-exchanger-ux

Compilar y ejecutar:
mvn clean install
mvn spring-boot:run