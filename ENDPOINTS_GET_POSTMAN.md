# 📋 Todos los Endpoints GET - Guía Rápida para Postman

## 🔐 IMPORTANTE: Todos requieren autenticación JWT
Excepto los endpoints de `/api/auth/**`, todos necesitan el header:
```
Authorization: Bearer {{token}}
```

---

## 📂 CATEGORÍA 1: ENLACES

### 1️⃣ **Listar TODOS los Enlaces**
```
GET {{baseUrl}}/api/enlaces
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna todos los enlaces del sistema con su último análisis.

**Respuesta de ejemplo:**
```json
[
  {
    "id": 1,
    "url": "https://paypal-verify.suspicious.com",
    "aplicacion": "Email",
    "mensaje": "Enlace sospechoso",
    "usuarioId": 1,
    "ultimoAnalisis": {
      "isPhishing": true,
      "probabilityPhishing": 0.94,
      "confidence": "HIGH",
      ...
    }
  },
  {
    "id": 2,
    "url": "https://www.google.com",
    "aplicacion": "Navegador",
    "mensaje": "Enlace legítimo",
    "usuarioId": 1,
    "ultimoAnalisis": {
      "isPhishing": false,
      "probabilityPhishing": 0.05,
      ...
    }
  }
]
```

---

### 2️⃣ **Obtener Enlace por ID**
```
GET {{baseUrl}}/api/enlaces/1
GET {{baseUrl}}/api/enlaces/2
GET {{baseUrl}}/api/enlaces/{id}
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna un enlace específico con su último análisis.

**Ejemplo de URL:**
```
http://localhost:8080/api/enlaces/1
```

**Respuesta de ejemplo:**
```json
{
  "id": 1,
  "url": "https://paypal-verify.suspicious.com",
  "aplicacion": "Email",
  "mensaje": "Enlace sospechoso",
  "usuarioId": 1,
  "ultimoAnalisis": {
    "id": 1,
    "enlaceId": 1,
    "isPhishing": true,
    "probabilityPhishing": 0.94,
    "confidence": "HIGH",
    "label": 0,
    "mlMessage": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
    "recommendation": "🚫 NO ACCEDER A ESTE ENLACE...",
    "urlLength": 39,
    "domain": "paypal-verify.suspicious.com",
    "hasHttps": true,
    "suspiciousKeywords": "paypal, verify",
    "analysisTimestamp": "2025-10-25T15:30:45.123456",
    "apiResponseTimeMs": 234
  }
}
```

**Posibles respuestas:**
- ✅ **200 OK** - Enlace encontrado
- ❌ **404 Not Found** - Enlace no existe

---

### 3️⃣ **Listar Enlaces por Usuario**
```
GET {{baseUrl}}/api/enlaces/usuario/1
GET {{baseUrl}}/api/enlaces/usuario/{usuarioId}
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna todos los enlaces creados por un usuario específico.

**Ejemplos de URLs:**
```
http://localhost:8080/api/enlaces/usuario/1
http://localhost:8080/api/enlaces/usuario/2
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 1,
    "url": "https://paypal-verify.suspicious.com",
    "aplicacion": "Email",
    "mensaje": "Enlace sospechoso",
    "usuarioId": 1,
    "ultimoAnalisis": { ... }
  },
  {
    "id": 3,
    "url": "https://facebook-login.xyz",
    "aplicacion": "Email",
    "mensaje": "Otro enlace",
    "usuarioId": 1,
    "ultimoAnalisis": { ... }
  }
]
```

---

## 🔬 CATEGORÍA 2: ANÁLISIS DE PHISHING

### 4️⃣ **Historial COMPLETO de Análisis de un Enlace**
```
GET {{baseUrl}}/api/analisis/enlace/1
GET {{baseUrl}}/api/analisis/enlace/{enlaceId}
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna TODOS los análisis realizados a un enlace específico (historial completo). Útil si se ha analizado la misma URL múltiples veces.

**Ejemplo de URL:**
```
http://localhost:8080/api/analisis/enlace/1
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 1,
    "enlaceId": 1,
    "isPhishing": true,
    "probabilityPhishing": 0.94,
    "confidence": "HIGH",
    "label": 0,
    "mlMessage": "⚠️ PELIGRO CRÍTICO...",
    "recommendation": "🚫 NO ACCEDER...",
    "urlLength": 39,
    "domain": "paypal-verify.suspicious.com",
    "domainLength": 31,
    "pathLength": 0,
    "protocol": "https",
    "hasHttps": true,
    "hasQuery": false,
    "specialCharactersCount": 3,
    "digitsInUrl": 0,
    "digitsInDomain": 0,
    "hasRepeatedDigits": false,
    "numberOfSubdomains": 1,
    "dotsInDomain": 2,
    "hyphensInDomain": 2,
    "suspiciousKeywordsCount": 2,
    "suspiciousKeywords": "paypal, verify",
    "analysisTimestamp": "2025-10-25T15:30:45.123456",
    "apiResponseTimeMs": 234,
    "analysisVersion": "1.0"
  },
  {
    "id": 5,
    "enlaceId": 1,
    "isPhishing": true,
    "probabilityPhishing": 0.92,
    "analysisTimestamp": "2025-10-25T16:45:12.654321",
    ...
  }
]
```

---

### 5️⃣ **ÚLTIMO Análisis de un Enlace**
```
GET {{baseUrl}}/api/analisis/enlace/1/ultimo
GET {{baseUrl}}/api/analisis/enlace/{enlaceId}/ultimo
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna solo el análisis MÁS RECIENTE de un enlace específico.

**Ejemplo de URL:**
```
http://localhost:8080/api/analisis/enlace/1/ultimo
```

**Respuesta de ejemplo:**
```json
{
  "id": 1,
  "enlaceId": 1,
  "isPhishing": true,
  "probabilityPhishing": 0.94,
  "confidence": "HIGH",
  "label": 0,
  "mlMessage": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
  "recommendation": "🚫 NO ACCEDER A ESTE ENLACE. Razones: contiene 2 palabras clave de phishing...",
  "urlLength": 39,
  "domain": "paypal-verify.suspicious.com",
  "domainLength": 31,
  "pathLength": 0,
  "protocol": "https",
  "hasHttps": true,
  "hasQuery": false,
  "specialCharactersCount": 3,
  "digitsInUrl": 0,
  "digitsInDomain": 0,
  "hasRepeatedDigits": false,
  "numberOfSubdomains": 1,
  "dotsInDomain": 2,
  "hyphensInDomain": 2,
  "suspiciousKeywordsCount": 2,
  "suspiciousKeywords": "paypal, verify",
  "analysisTimestamp": "2025-10-25T15:30:45.123456",
  "apiResponseTimeMs": 234,
  "analysisVersion": "1.0"
}
```

**Posibles respuestas:**
- ✅ **200 OK** - Análisis encontrado
- ❌ **404 Not Found** - No hay análisis para ese enlace

---

### 6️⃣ **TODOS los Análisis que Detectaron PHISHING**
```
GET {{baseUrl}}/api/analisis/phishing
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna todos los análisis donde `isPhishing = true`. Útil para ver un reporte de amenazas detectadas.

**Ejemplo de URL:**
```
http://localhost:8080/api/analisis/phishing
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 1,
    "enlaceId": 1,
    "isPhishing": true,
    "probabilityPhishing": 0.94,
    "confidence": "HIGH",
    "mlMessage": "⚠️ PELIGRO CRÍTICO...",
    "domain": "paypal-verify.suspicious.com",
    "suspiciousKeywords": "paypal, verify",
    "analysisTimestamp": "2025-10-25T15:30:45.123456",
    ...
  },
  {
    "id": 3,
    "enlaceId": 3,
    "isPhishing": true,
    "probabilityPhishing": 0.89,
    "confidence": "HIGH",
    "mlMessage": "⚠️ PELIGRO CRÍTICO...",
    "domain": "microsoft-verify.info",
    "suspiciousKeywords": "microsoft, verify",
    "analysisTimestamp": "2025-10-25T16:20:11.987654",
    ...
  }
]
```

---

### 7️⃣ **Obtener Análisis Específico por ID**
```
GET {{baseUrl}}/api/analisis/1
GET {{baseUrl}}/api/analisis/{id}
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Retorna un análisis específico por su ID.

**Ejemplos de URLs:**
```
http://localhost:8080/api/analisis/1
http://localhost:8080/api/analisis/2
```

**Respuesta de ejemplo:**
```json
{
  "id": 1,
  "enlaceId": 1,
  "isPhishing": true,
  "probabilityPhishing": 0.94,
  "confidence": "HIGH",
  "label": 0,
  "mlMessage": "⚠️ PELIGRO CRÍTICO: Esta URL es altamente sospechosa de phishing",
  "recommendation": "🚫 NO ACCEDER A ESTE ENLACE...",
  "urlLength": 39,
  "domain": "paypal-verify.suspicious.com",
  "domainLength": 31,
  "pathLength": 0,
  "protocol": "https",
  "hasHttps": true,
  "hasQuery": false,
  "specialCharactersCount": 3,
  "digitsInUrl": 0,
  "digitsInDomain": 0,
  "hasRepeatedDigits": false,
  "numberOfSubdomains": 1,
  "dotsInDomain": 2,
  "hyphensInDomain": 2,
  "suspiciousKeywordsCount": 2,
  "suspiciousKeywords": "paypal, verify",
  "analysisTimestamp": "2025-10-25T15:30:45.123456",
  "apiResponseTimeMs": 234,
  "analysisVersion": "1.0"
}
```

**Posibles respuestas:**
- ✅ **200 OK** - Análisis encontrado
- ❌ **404 Not Found** - Análisis no existe

---

## 🏥 CATEGORÍA 3: VERIFICACIÓN DE ESTADO

### 8️⃣ **Verificar Estado de la API de ML**
```
GET {{baseUrl}}/api/phishing/health
```
**Headers:**
```
Authorization: Bearer {{token}}
```

**Descripción:** Verifica si la API de Machine Learning (FastAPI) está disponible y funcionando.

**Ejemplo de URL:**
```
http://localhost:8080/api/phishing/health
```

**Respuesta esperada (API disponible):**
```json
{
  "ml_api_available": true,
  "status": "OK"
}
```

**Respuesta (API no disponible):**
```json
{
  "ml_api_available": false,
  "status": "UNAVAILABLE"
}
```

---

## 🤖 CATEGORÍA 4: API DE ML DIRECTA (FastAPI)

### 9️⃣ **Health Check de la API ML (Sin autenticación)**
```
GET {{mlApiUrl}}/health
```
**Headers:** _(ninguno, es público)_

**Descripción:** Verifica directamente la API de FastAPI (sin pasar por Spring Boot).

**Ejemplo de URL:**
```
http://localhost:8000/health
```

**Respuesta esperada:**
```json
{
  "status": "ok",
  "model_loaded": true,
  "model_type": "Ensemble Soft Voting (RF + XGBoost)"
}
```

---

### 🔟 **Información de la API ML (Sin autenticación)**
```
GET {{mlApiUrl}}/
```
**Headers:** _(ninguno, es público)_

**Descripción:** Obtiene información general de la API de FastAPI.

**Ejemplo de URL:**
```
http://localhost:8000/
```

**Respuesta esperada:**
```json
{
  "message": "Phishing Detection API",
  "version": "2.0",
  "endpoints": {
    "POST /predict-url": "Detecta phishing desde URL (respuesta simple)",
    "POST /predict-url-detailed": "Detecta phishing con análisis completo para reportes",
    "POST /predict": "Detecta phishing desde características",
    "GET /health": "Verifica estado de la API",
    "GET /docs": "Documentación interactiva"
  }
}
```

---

## 📊 RESUMEN DE TODOS LOS GET

### Enlaces (3 endpoints)
1. `GET /api/enlaces` - Listar todos
2. `GET /api/enlaces/{id}` - Por ID
3. `GET /api/enlaces/usuario/{usuarioId}` - Por usuario

### Análisis (4 endpoints)
4. `GET /api/analisis/enlace/{enlaceId}` - Historial completo
5. `GET /api/analisis/enlace/{enlaceId}/ultimo` - Último análisis
6. `GET /api/analisis/phishing` - Solo phishing detectados
7. `GET /api/analisis/{id}` - Por ID de análisis

### Estado (3 endpoints)
8. `GET /api/phishing/health` - Estado API ML (desde Spring Boot)
9. `GET {mlApiUrl}/health` - Estado API ML (directo)
10. `GET {mlApiUrl}/` - Info de la API ML

---

## 🎯 CASOS DE USO COMUNES

### **Ver todos los enlaces de un usuario**
```
GET http://localhost:8080/api/enlaces/usuario/1
Authorization: Bearer {{token}}
```

### **Ver solo los phishing detectados**
```
GET http://localhost:8080/api/analisis/phishing
Authorization: Bearer {{token}}
```

### **Verificar el último análisis de un enlace**
```
GET http://localhost:8080/api/analisis/enlace/1/ultimo
Authorization: Bearer {{token}}
```

### **Verificar si la API de ML está funcionando**
```
GET http://localhost:8080/api/phishing/health
Authorization: Bearer {{token}}
```

---

## 🔧 VARIABLES PARA POSTMAN

Configura estas variables en tu colección:

| Variable | Valor |
|----------|-------|
| `{{baseUrl}}` | `http://localhost:8080` |
| `{{mlApiUrl}}` | `http://localhost:8000` |
| `{{token}}` | _(se guarda automáticamente al hacer login)_ |
| `{{usuarioId}}` | `1` |

---

## ⚡ TIPS RÁPIDOS

### **1. Ver todos los enlaces con análisis**
```
GET /api/enlaces
```
Usa esto para ver un dashboard completo.

### **2. Ver historial de un enlace**
```
GET /api/analisis/enlace/1
```
Útil si quieres ver cómo cambia el análisis con el tiempo.

### **3. Filtrar solo amenazas**
```
GET /api/analisis/phishing
```
Perfecto para un reporte de seguridad.

### **4. Verificar estado del sistema**
```
GET /api/phishing/health
GET {{mlApiUrl}}/health
```
Para monitoreo y debugging.

---

## 🧪 SECUENCIA DE PRUEBA RECOMENDADA

```
1. Login para obtener token
   POST /api/auth/login

2. Crear algunos enlaces
   POST /api/enlaces (2-3 veces con URLs diferentes)

3. Ver todos los enlaces
   GET /api/enlaces

4. Ver enlaces del usuario
   GET /api/enlaces/usuario/1

5. Ver un enlace específico
   GET /api/enlaces/1

6. Ver último análisis
   GET /api/analisis/enlace/1/ultimo

7. Ver todos los phishing
   GET /api/analisis/phishing

8. Verificar estado ML
   GET /api/phishing/health
```

---

## ❌ ERRORES COMUNES

### **401 Unauthorized**
**Causa:** Token no válido o falta el header `Authorization`

**Solución:**
- Asegúrate de incluir: `Authorization: Bearer {{token}}`
- Verifica que el token no haya expirado (24 horas)
- Haz login nuevamente

### **404 Not Found**
**Causa:** El ID del recurso no existe

**Solución:**
- Verifica que el ID exista en la base de datos
- Prueba con `GET /api/enlaces` primero para ver los IDs disponibles

### **500 Internal Server Error**
**Causa:** Error en el servidor (posiblemente la API de ML no está disponible)

**Solución:**
- Verifica que la API de ML esté corriendo: `GET {{mlApiUrl}}/health`
- Revisa los logs de Spring Boot

---

¡Ahora tienes todos los GET endpoints documentados! 🎉
