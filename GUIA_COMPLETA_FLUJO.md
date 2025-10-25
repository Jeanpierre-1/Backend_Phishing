# 🚀 Guía Completa: Flujo de Usuario desde Cero

## 📋 Requisitos Previos

1. **PostgreSQL** corriendo en puerto 5433
2. **Base de datos** `db_phishing` creada
3. **API de FastAPI** corriendo en puerto 8000
4. **Backend Spring Boot** listo para iniciar

---

## 🗄️ PASO 1: Preparar la Base de Datos

### 1.1 Conectarse a PostgreSQL

```powershell
# Abrir PowerShell y conectarse a PostgreSQL
psql -U postgres -p 5433
```

### 1.2 Crear la base de datos (si no existe)

```sql
-- Crear la base de datos
CREATE DATABASE db_phishing;

-- Conectarse a la base de datos
\c db_phishing

-- Verificar que esté vacía o lista
\dt
```

### 1.3 Salir de PostgreSQL

```sql
\q
```

---

## 🤖 PASO 2: Iniciar la API de Machine Learning

### 2.1 Navegar al directorio de la API

```powershell
cd d:\ML-ASEMBLE
```

### 2.2 Verificar que existan los archivos necesarios

```powershell
dir
# Debe mostrar:
# - prediction_api_test.py
# - url_feature_extractor.py
# - ensemble_soft_voting.pkl (o el modelo que uses)
```

### 2.3 Iniciar la API de FastAPI

```powershell
python prediction_api_test.py
```

**Salida esperada:**
```
Cargando modelo desde ensemble_soft_voting.pkl...
✅ Modelo cargado correctamente
INFO:     Started server process [12345]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
INFO:     Uvicorn running on http://0.0.0.0:8000
```

### 2.4 Verificar que la API funcione

Abre **otro PowerShell** y ejecuta:

```powershell
curl http://localhost:8000/health
```

**Respuesta esperada:**
```json
{
  "status": "ok",
  "model_loaded": true,
  "model_type": "Ensemble Soft Voting (RF + XGBoost)"
}
```

✅ **Deja esta terminal abierta con la API corriendo**

---

## ☕ PASO 3: Iniciar el Backend de Spring Boot

### 3.1 Abrir una NUEVA terminal PowerShell

### 3.2 Navegar al proyecto del backend

```powershell
cd c:\Users\USER\OneDrive\Documents\GitHub\Backend_Phishing
```

### 3.3 Compilar y ejecutar

```powershell
# Compilar (opcional, si hiciste cambios)
.\mvnw clean install -DskipTests

# Ejecutar
.\mvnw spring-boot:run
```

**Salida esperada:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.4)

...
Started GestorInventarioBackendApplication in 5.234 seconds
```

### 3.4 Verificar las tablas creadas

En **otra terminal**, conectarse a PostgreSQL:

```powershell
psql -U postgres -p 5433 -d db_phishing
```

Ver las tablas creadas:

```sql
\dt

-- Deberías ver:
-- analisis_phishing
-- enlaces
-- roles
-- usuarios
-- usuarios_roles
```

Ver estructura de la tabla de análisis:

```sql
\d analisis_phishing
```

Salir:

```sql
\q
```

✅ **Deja esta terminal abierta con Spring Boot corriendo**

---

## 👤 PASO 4: Registrar un Usuario

### 4.1 Abrir una NUEVA terminal PowerShell (o usar Postman/Insomnia)

### 4.2 Registrar un usuario nuevo

```powershell
curl -X POST http://localhost:8080/api/auth/registro `
  -H "Content-Type: application/json" `
  -d '{
    "username": "juanperez",
    "password": "password123",
    "nombre": "Juan",
    "apellido": "Perez",
    "roles": ["ROLE_USER"]
  }'
```

**Respuesta esperada:**
```json
{
  "message": "Usuario registrado exitosamente"
}
```

### 4.3 Registrar un administrador (opcional)

```powershell
curl -X POST http://localhost:8080/api/auth/registro `
  -H "Content-Type: application/json" `
  -d '{
    "username": "admin",
    "password": "admin123",
    "nombre": "Administrador",
    "apellido": "Sistema",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  }'
```

---

## 🔐 PASO 5: Iniciar Sesión (Obtener Token JWT)

### 5.1 Login del usuario

```powershell
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{
    "username": "juanperez",
    "password": "password123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucGVyZXoiLCJpYXQiOjE3Mjk4..."
}
```

### 5.2 Copiar el token

📝 **IMPORTANTE:** Copia el valor del token (todo el texto después de `"token": "`). Lo necesitarás para los siguientes pasos.

Ejemplo:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucGVyZXoiLCJpYXQiOjE3Mjk4...
```

---

## 🔗 PASO 6: Crear un Enlace (CON ANÁLISIS AUTOMÁTICO)

### 6.1 Verificar que tienes el ID del usuario

Primero, obtén el ID del usuario. Si seguiste los pasos, el primer usuario tiene ID = 1.

Para verificar, puedes consultar la base de datos:

```powershell
psql -U postgres -p 5433 -d db_phishing -c "SELECT id, username, nombre FROM usuarios;"
```

### 6.2 Crear un enlace sospechoso (ejemplo de phishing)

```powershell
# Reemplaza TU_TOKEN con el token que copiaste
$token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucGVyZXoiLCJpYXQiOjE3Mjk4..."

curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://paypal-verify-account.suspicious-domain.com/secure/login",
    "aplicacion": "Email Corporativo",
    "mensaje": "Recibí este enlace en un correo que dice que mi cuenta será suspendida",
    "usuarioId": 1
  }'
```

**Respuesta esperada (CON ANÁLISIS):**
```json
{
  "id": 1,
  "url": "https://paypal-verify-account.suspicious-domain.com/secure/login",
  "aplicacion": "Email Corporativo",
  "mensaje": "Recibí este enlace en un correo que dice que mi cuenta será suspendida",
  "usuarioId": 1,
  "ultimoAnalisis": {
    "id": 1,
    "enlaceId": 1,
    "isPhishing": true,
    "probabilityPhishing": 0.94,
    "confidence": "HIGH",
    "label": 0,
    "mlMessage": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
    "recommendation": "🚫 NO ACCEDER A ESTE ENLACE. Razones: contiene 3 palabras clave de phishing...",
    "urlLength": 69,
    "domain": "paypal-verify-account.suspicious-domain.com",
    "domainLength": 45,
    "pathLength": 14,
    "protocol": "https",
    "hasHttps": true,
    "hasQuery": false,
    "specialCharactersCount": 5,
    "digitsInUrl": 0,
    "digitsInDomain": 0,
    "hasRepeatedDigits": false,
    "numberOfSubdomains": 2,
    "dotsInDomain": 3,
    "hyphensInDomain": 3,
    "suspiciousKeywordsCount": 3,
    "suspiciousKeywords": "paypal, verify, account",
    "analysisTimestamp": "2025-10-25T15:30:45.123456",
    "apiResponseTimeMs": 234,
    "analysisVersion": "1.0"
  }
}
```

### 6.3 Crear un enlace legítimo (ejemplo seguro)

```powershell
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://www.google.com",
    "aplicacion": "Navegador",
    "mensaje": "Enlace de prueba legítimo",
    "usuarioId": 1
  }'
```

**Respuesta esperada:**
```json
{
  "id": 2,
  "url": "https://www.google.com",
  "aplicacion": "Navegador",
  "mensaje": "Enlace de prueba legítimo",
  "usuarioId": 1,
  "ultimoAnalisis": {
    "id": 2,
    "enlaceId": 2,
    "isPhishing": false,
    "probabilityPhishing": 0.05,
    "confidence": "HIGH",
    "label": 1,
    "mlMessage": "✅ SEGURO: Esta URL parece completamente legítima",
    "recommendation": "✅ Puede acceder con confianza...",
    ...
  }
}
```

---

## 📊 PASO 7: Consultar Enlaces y Análisis

### 7.1 Listar todos los enlaces del usuario

```powershell
curl -X GET http://localhost:8080/api/enlaces/usuario/1 `
  -H "Authorization: Bearer $token"
```

### 7.2 Obtener un enlace específico

```powershell
curl -X GET http://localhost:8080/api/enlaces/1 `
  -H "Authorization: Bearer $token"
```

### 7.3 Obtener todos los análisis de un enlace (historial)

```powershell
curl -X GET http://localhost:8080/api/analisis/enlace/1 `
  -H "Authorization: Bearer $token"
```

### 7.4 Obtener solo el último análisis de un enlace

```powershell
curl -X GET http://localhost:8080/api/analisis/enlace/1/ultimo `
  -H "Authorization: Bearer $token"
```

### 7.5 Obtener todos los análisis que detectaron phishing

```powershell
curl -X GET http://localhost:8080/api/analisis/phishing `
  -H "Authorization: Bearer $token"
```

---

## 🔍 PASO 8: Analizar URL sin Guardar Enlace

### 8.1 Análisis rápido (sin guardar en BD)

```powershell
curl -X POST http://localhost:8080/api/phishing/analyze `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://amazon-security-alert.malicious.com/update"
  }'
```

### 8.2 Análisis detallado

```powershell
curl -X POST http://localhost:8080/api/phishing/analyze-detailed `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://facebook-verify-identity.xyz/login"
  }'
```

---

## 🗄️ PASO 9: Verificar Datos en la Base de Datos

### 9.1 Conectarse a PostgreSQL

```powershell
psql -U postgres -p 5433 -d db_phishing
```

### 9.2 Ver usuarios creados

```sql
SELECT id, username, nombre, apellido FROM usuarios;
```

### 9.3 Ver enlaces creados

```sql
SELECT id, url, aplicacion, usuario_id FROM enlaces;
```

### 9.4 Ver análisis realizados

```sql
SELECT 
    id, 
    enlace_id, 
    is_phishing, 
    probability_phishing, 
    confidence,
    ml_message,
    analysis_timestamp 
FROM analisis_phishing 
ORDER BY analysis_timestamp DESC;
```

### 9.5 Ver análisis con sus enlaces (JOIN)

```sql
SELECT 
    e.id as enlace_id,
    e.url,
    a.is_phishing,
    a.probability_phishing,
    a.confidence,
    a.ml_message,
    a.analysis_timestamp
FROM enlaces e
LEFT JOIN analisis_phishing a ON e.id = a.enlace_id
ORDER BY a.analysis_timestamp DESC;
```

### 9.6 Contar análisis de phishing detectados

```sql
SELECT 
    COUNT(*) as total_analisis,
    SUM(CASE WHEN is_phishing = true THEN 1 ELSE 0 END) as phishing_detectado,
    SUM(CASE WHEN is_phishing = false THEN 1 ELSE 0 END) as legitimos
FROM analisis_phishing;
```

### 9.7 Salir

```sql
\q
```

---

## 🧪 PASO 10: Ejemplos de URLs para Probar

### URLs Sospechosas (probablemente phishing)

```powershell
# PayPal falso
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "http://paypal-security-update.tk/verify-account",
    "aplicacion": "Email",
    "mensaje": "Solicita verificar cuenta urgente",
    "usuarioId": 1
  }'

# Banco falso
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://secure-banking-login-12345.xyz/authenticate",
    "aplicacion": "SMS",
    "mensaje": "Mensaje de supuesto banco",
    "usuarioId": 1
  }'

# Microsoft falso
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "http://microsoft-account-verify.info/secure/login.php",
    "aplicacion": "Email",
    "mensaje": "Verificación de cuenta Microsoft",
    "usuarioId": 1
  }'
```

### URLs Legítimas

```powershell
# GitHub
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://github.com/Jeanpierre-1/Backend_Phishing",
    "aplicacion": "Navegador",
    "mensaje": "Repositorio del proyecto",
    "usuarioId": 1
  }'

# Universidad
curl -X POST http://localhost:8080/api/enlaces `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://www.universidad.edu.pe/portal/estudiantes",
    "aplicacion": "Email Institucional",
    "mensaje": "Portal de estudiantes",
    "usuarioId": 1
  }'
```

---

## 🔄 PASO 11: Actualizar y Eliminar

### 11.1 Actualizar un enlace

```powershell
curl -X PUT http://localhost:8080/api/enlaces/1 `
  -H "Content-Type: application/json" `
  -H "Authorization: Bearer $token" `
  -d '{
    "url": "https://paypal-verify-account.suspicious-domain.com/secure/login",
    "aplicacion": "Email Corporativo - ACTUALIZADO",
    "mensaje": "Este enlace fue marcado como sospechoso por el usuario",
    "usuarioId": 1
  }'
```

### 11.2 Eliminar un enlace

```powershell
# Esto también eliminará sus análisis (cascade)
curl -X DELETE http://localhost:8080/api/enlaces/1 `
  -H "Authorization: Bearer $token"
```

---

## 📈 PASO 12: Verificar Estado del Sistema

### 12.1 Verificar API de ML

```powershell
curl http://localhost:8000/
```

### 12.2 Verificar salud de la API de ML desde el backend

```powershell
curl -X GET http://localhost:8080/api/phishing/health `
  -H "Authorization: Bearer $token"
```

**Respuesta esperada:**
```json
{
  "ml_api_available": true,
  "status": "OK"
}
```

---

## 🎯 RESUMEN DEL FLUJO COMPLETO

```
1. PostgreSQL corriendo → Base de datos db_phishing creada
2. API FastAPI corriendo → Modelo ML cargado en http://localhost:8000
3. Spring Boot corriendo → Backend en http://localhost:8080
4. Usuario registrado → POST /api/auth/registro
5. Login exitoso → POST /api/auth/login → Token JWT obtenido
6. Crear enlace → POST /api/enlaces → Se analiza automáticamente
7. Resultado guardado → Tabla enlaces + Tabla analisis_phishing
8. Consultar datos → GET /api/enlaces, /api/analisis
```

---

## 🐛 Solución de Problemas

### Error: "No se pudo conectar con la API de detección de phishing"

**Causa:** La API de FastAPI no está corriendo.

**Solución:**
```powershell
cd d:\ML-ASEMBLE
python prediction_api_test.py
```

### Error: "Usuario no encontrado"

**Causa:** El usuarioId no existe en la base de datos.

**Solución:**
```sql
-- Verificar IDs de usuarios
SELECT id, username FROM usuarios;
```

### Error: "401 Unauthorized"

**Causa:** Token JWT expirado o inválido.

**Solución:** Volver a hacer login para obtener un nuevo token.

### Error: "Could not connect to database"

**Causa:** PostgreSQL no está corriendo o la configuración es incorrecta.

**Solución:**
```powershell
# Verificar si PostgreSQL está corriendo
Get-Service -Name postgresql*

# Verificar conexión
psql -U postgres -p 5433 -d db_phishing
```

---

## 📚 Endpoints Completos de Referencia

### Autenticación (Públicos)
- `POST /api/auth/registro` - Registrar usuario
- `POST /api/auth/login` - Iniciar sesión

### Enlaces (Requieren JWT)
- `POST /api/enlaces` - Crear enlace (con análisis automático)
- `GET /api/enlaces` - Listar todos
- `GET /api/enlaces/{id}` - Obtener por ID
- `GET /api/enlaces/usuario/{usuarioId}` - Por usuario
- `PUT /api/enlaces/{id}` - Actualizar
- `DELETE /api/enlaces/{id}` - Eliminar

### Análisis de Phishing (Requieren JWT)
- `GET /api/analisis/enlace/{enlaceId}` - Historial completo de análisis
- `GET /api/analisis/enlace/{enlaceId}/ultimo` - Último análisis
- `GET /api/analisis/phishing` - Todos los phishing detectados
- `GET /api/analisis/{id}` - Análisis específico
- `POST /api/phishing/analyze` - Analizar sin guardar (simple)
- `POST /api/phishing/analyze-detailed` - Analizar sin guardar (detallado)
- `GET /api/phishing/health` - Estado de API ML

---

## ✅ Checklist Final

- [ ] PostgreSQL corriendo en puerto 5433
- [ ] Base de datos `db_phishing` creada
- [ ] API FastAPI corriendo en puerto 8000
- [ ] Spring Boot corriendo en puerto 8080
- [ ] Usuario registrado exitosamente
- [ ] Token JWT obtenido
- [ ] Primer enlace creado con análisis
- [ ] Datos verificados en PostgreSQL

¡Listo! 🎉
