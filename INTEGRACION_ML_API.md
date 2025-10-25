# Integración API de Detección de Phishing

## 📋 Resumen de Cambios

Se ha integrado exitosamente la API de Machine Learning de detección de phishing con el backend de Spring Boot. Ahora cuando un usuario ingresa un enlace, automáticamente se analiza con el modelo de ML y se guardan los resultados.

## 🆕 Archivos Creados

### DTOs
- **`PhishingDetectionResponse.java`**: Mapea la respuesta completa de la API de FastAPI
- **`PhishingAnalysisDTO.java`**: DTO simplificado para respuestas al frontend

### Servicios
- **`IPhishingDetectionService.java`**: Interfaz del servicio de detección
- **`PhishingDetectionServiceImpl.java`**: Implementación que se comunica con la API de FastAPI

### Controladores
- **`PhishingAnalysisController.java`**: Nuevo controlador para análisis directo de URLs

## 🔄 Archivos Modificados

### Entidad `Enlace.java`
Se agregaron nuevos campos para almacenar los resultados del análisis:
- `isPhishing` - Si la URL es phishing (Boolean)
- `probabilityPhishing` - Probabilidad de ser phishing (0.0 - 1.0)
- `confidence` - Nivel de confianza (HIGH, MEDIUM, LOW)
- `mlMessage` - Mensaje descriptivo del análisis
- `recommendation` - Recomendación de seguridad
- `analysisTimestamp` - Marca de tiempo del análisis

### DTO `EnlaceDTO.java`
Se agregaron los mismos campos para transferir los datos del análisis.

### Servicio `EnlaceImpl.java`
Ahora al insertar un enlace:
1. Se llama automáticamente a la API de ML
2. Se analiza la URL
3. Se guardan los resultados en la base de datos
4. Si la API falla, se registra el error pero se guarda el enlace de todas formas

### Configuración `application.properties`
Se agregó la URL de la API de ML:
```properties
phishing.api.url=http://localhost:8000
```

## 🚀 Uso

### 1. Iniciar la API de FastAPI

Primero, asegúrate de que tu API de ML esté corriendo:

```bash
cd d:\ML-ASEMBLE
python prediction_api_test.py
```

La API estará disponible en: `http://localhost:8000`

### 2. Iniciar el Backend de Spring Boot

```bash
cd c:\Users\USER\OneDrive\Documents\GitHub\Backend_Phishing
mvnw spring-boot:run
```

## 📡 Endpoints Disponibles

### Endpoint Original (CON ANÁLISIS AUTOMÁTICO)

**POST** `/api/enlaces`

Ahora este endpoint analiza automáticamente la URL con ML al crear un enlace.

**Request:**
```json
{
  "url": "https://ejemplo-sospechoso.com/login",
  "aplicacion": "Email",
  "mensaje": "Enlace recibido por correo",
  "usuarioId": 1
}
```

**Response:**
```json
{
  "id": 1,
  "url": "https://ejemplo-sospechoso.com/login",
  "aplicacion": "Email",
  "mensaje": "Enlace recibido por correo",
  "usuarioId": 1,
  "isPhishing": true,
  "probabilityPhishing": 0.89,
  "confidence": "HIGH",
  "mlMessage": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
  "recommendation": "🚫 NO ACCEDER A ESTE ENLACE. Razones: contiene 2 palabras clave de phishing...",
  "analysisTimestamp": "2025-10-25T10:30:45.123456"
}
```

### Nuevos Endpoints de Análisis Directo

#### Análisis Simple

**POST** `/api/phishing/analyze`

Analiza una URL sin guardarla en la base de datos.

**Request:**
```json
{
  "url": "https://ejemplo.com/login"
}
```

**Response:**
```json
{
  "url": "https://ejemplo.com/login",
  "isPhishing": true,
  "probabilityPhishing": 0.89,
  "confidence": "HIGH",
  "message": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
  "recommendation": "🚫 NO ACCEDER A ESTE ENLACE...",
  "timestamp": "2025-10-25T10:30:45.123456",
  "urlLength": 28,
  "domain": "ejemplo.com",
  "hasHttps": true,
  "suspiciousKeywordsCount": 1,
  "specialCharactersCount": 3
}
```

#### Análisis Detallado

**POST** `/api/phishing/analyze-detailed`

Obtiene el análisis completo con todos los detalles.

**Request:**
```json
{
  "url": "https://ejemplo.com/login"
}
```

**Response:** (Respuesta completa de la API de ML con todos los campos)

#### Verificar Estado

**GET** `/api/phishing/health`

Verifica si la API de ML está disponible.

**Response:**
```json
{
  "ml_api_available": true,
  "status": "OK"
}
```

### Endpoints Existentes (sin cambios)

- **GET** `/api/enlaces` - Listar todos los enlaces
- **GET** `/api/enlaces/usuario/{usuarioId}` - Enlaces por usuario
- **GET** `/api/enlaces/{id}` - Obtener enlace por ID
- **PUT** `/api/enlaces/{id}` - Actualizar enlace
- **DELETE** `/api/enlaces/{id}` - Eliminar enlace

## 🗄️ Actualización de Base de Datos

Al iniciar la aplicación, Hibernate creará automáticamente las nuevas columnas en la tabla `enlaces`:

```sql
ALTER TABLE enlaces ADD COLUMN is_phishing BOOLEAN;
ALTER TABLE enlaces ADD COLUMN probability_phishing DOUBLE PRECISION;
ALTER TABLE enlaces ADD COLUMN confidence VARCHAR(255);
ALTER TABLE enlaces ADD COLUMN ml_message VARCHAR(500);
ALTER TABLE enlaces ADD COLUMN recommendation VARCHAR(1000);
ALTER TABLE enlaces ADD COLUMN analysis_timestamp VARCHAR(255);
```

## 🔒 Seguridad

Los endpoints de análisis de phishing **requieren autenticación JWT** como todos los demás endpoints (excepto `/api/auth/**`).

Para probar, necesitas:

1. Obtener un token JWT:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tu_usuario","password":"tu_password"}'
```

2. Usar el token en las peticiones:
```bash
curl -X POST http://localhost:8080/api/enlaces \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_JWT" \
  -d '{
    "url": "https://ejemplo.com",
    "aplicacion": "Email",
    "mensaje": "Test",
    "usuarioId": 1
  }'
```

## ⚙️ Configuración

Puedes cambiar la URL de la API de ML en `application.properties`:

```properties
# Producción
phishing.api.url=http://api-ml.tudominio.com

# Local
phishing.api.url=http://localhost:8000

# Docker
phishing.api.url=http://ml-api:8000
```

## 🛠️ Manejo de Errores

Si la API de ML no está disponible:
- El enlace se guarda de todas formas
- Los campos de análisis quedan en `null` o con valores por defecto
- Se registra el error en los logs
- El sistema continúa funcionando normalmente

## 📊 Flujo de Trabajo

```
Usuario -> POST /api/enlaces con URL
    ↓
Backend recibe la petición
    ↓
Se valida el usuario
    ↓
Se llama a la API de ML (http://localhost:8000/predict-url-detailed)
    ↓
API de ML analiza la URL y devuelve resultados
    ↓
Backend guarda el enlace + resultados del análisis en PostgreSQL
    ↓
Se devuelve al usuario el enlace con el análisis completo
```

## 🧪 Pruebas

### Probar la API de ML directamente:
```bash
curl -X POST http://localhost:8000/predict-url-detailed \
  -H "Content-Type: application/json" \
  -d '{"url": "https://ejemplo.com"}'
```

### Probar el análisis directo:
```bash
curl -X POST http://localhost:8080/api/phishing/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN" \
  -d '{"url": "https://ejemplo.com"}'
```

### Probar creación de enlace con análisis:
```bash
curl -X POST http://localhost:8080/api/enlaces \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN" \
  -d '{
    "url": "https://paypal-verify-account.suspicious.com",
    "aplicacion": "Email Corporativo",
    "mensaje": "Enlace recibido solicitando verificación",
    "usuarioId": 1
  }'
```

## 🎯 Próximos Pasos

1. **Implementar caché** para no analizar URLs repetidas
2. **Agregar historial** de análisis por URL
3. **Notificaciones** cuando se detecta phishing de alta probabilidad
4. **Dashboard** con estadísticas de detección
5. **Reentrenamiento** del modelo con los enlaces reportados
