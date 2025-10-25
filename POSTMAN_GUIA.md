# 📬 Guía Completa de Postman - API de Detección de Phishing

## 📥 OPCIÓN 1: Importar Colección (Recomendado)

Guarda el siguiente JSON como `Phishing_Detection_API.postman_collection.json` e impórtalo en Postman.

```json
{
  "info": {
    "name": "Phishing Detection API",
    "description": "API para detección de phishing con ML",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080",
      "type": "string"
    },
    {
      "key": "mlApiUrl",
      "value": "http://localhost:8000",
      "type": "string"
    },
    {
      "key": "token",
      "value": "",
      "type": "string"
    },
    {
      "key": "usuarioId",
      "value": "1",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "1. Autenticación",
      "item": [
        {
          "name": "Registrar Usuario",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"juanperez\",\n  \"password\": \"password123\",\n  \"nombre\": \"Juan\",\n  \"apellido\": \"Perez\",\n  \"roles\": [\"ROLE_USER\"]\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/registro",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "registro"]
            }
          },
          "response": []
        },
        {
          "name": "Login (Obtener Token)",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "var jsonData = pm.response.json();",
                  "pm.collectionVariables.set(\"token\", jsonData.token);"
                ],
                "type": "text/javascript"
              }
            }
          ],
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"juanperez\",\n  \"password\": \"password123\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/login",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "login"]
            }
          },
          "response": []
        },
        {
          "name": "Registrar Admin",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\",\n  \"nombre\": \"Administrador\",\n  \"apellido\": \"Sistema\",\n  \"roles\": [\"ROLE_ADMIN\", \"ROLE_USER\"]\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/registro",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "registro"]
            }
          },
          "response": []
        }
      ]
    },
    {
      "name": "2. Enlaces (Con Análisis Automático)",
      "item": [
        {
          "name": "Crear Enlace Sospechoso",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://paypal-verify-account.suspicious-domain.com/secure/login\",\n  \"aplicacion\": \"Email Corporativo\",\n  \"mensaje\": \"Recibí este enlace en un correo que dice que mi cuenta será suspendida\",\n  \"usuarioId\": {{usuarioId}}\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/enlaces",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces"]
            }
          },
          "response": []
        },
        {
          "name": "Crear Enlace Legítimo",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://www.google.com\",\n  \"aplicacion\": \"Navegador\",\n  \"mensaje\": \"Enlace de prueba legítimo\",\n  \"usuarioId\": {{usuarioId}}\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/enlaces",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces"]
            }
          },
          "response": []
        },
        {
          "name": "Listar Todos los Enlaces",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/enlaces",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces"]
            }
          },
          "response": []
        },
        {
          "name": "Listar Enlaces por Usuario",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/enlaces/usuario/{{usuarioId}}",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces", "usuario", "{{usuarioId}}"]
            }
          },
          "response": []
        },
        {
          "name": "Obtener Enlace por ID",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/enlaces/1",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces", "1"]
            }
          },
          "response": []
        },
        {
          "name": "Actualizar Enlace",
          "request": {
            "method": "PUT",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://paypal-verify-account.suspicious-domain.com/secure/login\",\n  \"aplicacion\": \"Email Corporativo - ACTUALIZADO\",\n  \"mensaje\": \"Este enlace fue marcado como sospechoso\",\n  \"usuarioId\": {{usuarioId}}\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/enlaces/1",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces", "1"]
            }
          },
          "response": []
        },
        {
          "name": "Eliminar Enlace",
          "request": {
            "method": "DELETE",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/enlaces/1",
              "host": ["{{baseUrl}}"],
              "path": ["api", "enlaces", "1"]
            }
          },
          "response": []
        }
      ]
    },
    {
      "name": "3. Análisis de Phishing",
      "item": [
        {
          "name": "Historial de Análisis de un Enlace",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/analisis/enlace/1",
              "host": ["{{baseUrl}}"],
              "path": ["api", "analisis", "enlace", "1"]
            }
          },
          "response": []
        },
        {
          "name": "Último Análisis de un Enlace",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/analisis/enlace/1/ultimo",
              "host": ["{{baseUrl}}"],
              "path": ["api", "analisis", "enlace", "1", "ultimo"]
            }
          },
          "response": []
        },
        {
          "name": "Todos los Phishing Detectados",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/analisis/phishing",
              "host": ["{{baseUrl}}"],
              "path": ["api", "analisis", "phishing"]
            }
          },
          "response": []
        },
        {
          "name": "Obtener Análisis por ID",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/analisis/1",
              "host": ["{{baseUrl}}"],
              "path": ["api", "analisis", "1"]
            }
          },
          "response": []
        }
      ]
    },
    {
      "name": "4. Análisis Directo (Sin Guardar)",
      "item": [
        {
          "name": "Analizar URL Simple",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://amazon-security-alert.malicious.com/update\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/phishing/analyze",
              "host": ["{{baseUrl}}"],
              "path": ["api", "phishing", "analyze"]
            }
          },
          "response": []
        },
        {
          "name": "Analizar URL Detallado",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              },
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://facebook-verify-identity.xyz/login\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/api/phishing/analyze-detailed",
              "host": ["{{baseUrl}}"],
              "path": ["api", "phishing", "analyze-detailed"]
            }
          },
          "response": []
        },
        {
          "name": "Verificar Estado API ML",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "{{baseUrl}}/api/phishing/health",
              "host": ["{{baseUrl}}"],
              "path": ["api", "phishing", "health"]
            }
          },
          "response": []
        }
      ]
    },
    {
      "name": "5. API ML Directa (FastAPI)",
      "item": [
        {
          "name": "Health Check ML API",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "{{mlApiUrl}}/health",
              "host": ["{{mlApiUrl}}"],
              "path": ["health"]
            }
          },
          "response": []
        },
        {
          "name": "Predict URL (ML API Directa)",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"url\": \"https://paypal-verify.suspicious.com\"\n}"
            },
            "url": {
              "raw": "{{mlApiUrl}}/predict-url-detailed",
              "host": ["{{mlApiUrl}}"],
              "path": ["predict-url-detailed"]
            }
          },
          "response": []
        }
      ]
    }
  ]
}
```

---

## 🎯 OPCIÓN 2: Configuración Manual Paso a Paso

### PASO 1: Configurar Variables de Entorno en Postman

1. **Crear una nueva colección** llamada `Phishing Detection API`
2. **Ir a la pestaña "Variables"** de la colección
3. **Agregar estas variables:**

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `baseUrl` | `http://localhost:8080` | `http://localhost:8080` |
| `mlApiUrl` | `http://localhost:8000` | `http://localhost:8000` |
| `token` | _(dejar vacío)_ | _(dejar vacío)_ |
| `usuarioId` | `1` | `1` |

---

### PASO 2: Crear Carpetas (Folders)

Organiza las peticiones en estas carpetas:
1. 📁 **1. Autenticación**
2. 📁 **2. Enlaces (Con Análisis Automático)**
3. 📁 **3. Análisis de Phishing**
4. 📁 **4. Análisis Directo (Sin Guardar)**
5. 📁 **5. API ML Directa (FastAPI)**

---

## 📝 PASO 3: Crear las Peticiones

### 📁 1. Autenticación

#### ✉️ **A. Registrar Usuario**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/auth/registro`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "username": "juanperez",
    "password": "password123",
    "nombre": "Juan",
    "apellido": "Perez",
    "roles": ["ROLE_USER"]
  }
  ```
- **Respuesta esperada:**
  ```json
  {
    "message": "Usuario registrado exitosamente"
  }
  ```

---

#### 🔑 **B. Login (Obtener Token JWT)**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/auth/login`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "username": "juanperez",
    "password": "password123"
  }
  ```
- **Respuesta esperada:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucGVyZXo..."
  }
  ```

**🔥 IMPORTANTE - Script para Auto-Guardar el Token:**

1. Ve a la pestaña **"Tests"** de esta petición
2. Agrega este script:

```javascript
var jsonData = pm.response.json();
pm.collectionVariables.set("token", jsonData.token);
console.log("Token guardado: " + jsonData.token);
```

Este script guardará automáticamente el token en la variable de colección.

---

### 📁 2. Enlaces (Con Análisis Automático)

#### 🔗 **A. Crear Enlace Sospechoso (Phishing)**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/enlaces`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://paypal-verify-account.suspicious-domain.com/secure/login",
    "aplicacion": "Email Corporativo",
    "mensaje": "Recibí este enlace en un correo que dice que mi cuenta será suspendida",
    "usuarioId": {{usuarioId}}
  }
  ```
- **Respuesta esperada:**
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
      "recommendation": "🚫 NO ACCEDER A ESTE ENLACE...",
      "suspiciousKeywords": "paypal, verify, account",
      "analysisTimestamp": "2025-10-25T15:30:45.123456"
    }
  }
  ```

---

#### ✅ **B. Crear Enlace Legítimo**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/enlaces`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://www.google.com",
    "aplicacion": "Navegador",
    "mensaje": "Enlace de prueba legítimo",
    "usuarioId": {{usuarioId}}
  }
  ```

---

#### 📋 **C. Listar Todos los Enlaces**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/enlaces`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```

---

#### 👤 **D. Listar Enlaces por Usuario**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/enlaces/usuario/{{usuarioId}}`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```

---

#### 🔍 **E. Obtener Enlace por ID**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/enlaces/1`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```

---

#### ✏️ **F. Actualizar Enlace**

- **Method:** `PUT`
- **URL:** `{{baseUrl}}/api/enlaces/1`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://paypal-verify-account.suspicious-domain.com/secure/login",
    "aplicacion": "Email Corporativo - ACTUALIZADO",
    "mensaje": "Este enlace fue marcado como sospechoso",
    "usuarioId": {{usuarioId}}
  }
  ```

---

#### 🗑️ **G. Eliminar Enlace**

- **Method:** `DELETE`
- **URL:** `{{baseUrl}}/api/enlaces/1`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```

---

### 📁 3. Análisis de Phishing

#### 📊 **A. Historial de Análisis de un Enlace**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/analisis/enlace/1`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```
- **Descripción:** Retorna TODOS los análisis realizados a ese enlace

---

#### 🕐 **B. Último Análisis de un Enlace**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/analisis/enlace/1/ultimo`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```
- **Descripción:** Retorna solo el análisis más reciente

---

#### ⚠️ **C. Todos los Phishing Detectados**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/analisis/phishing`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```
- **Descripción:** Lista todos los análisis donde se detectó phishing

---

#### 🆔 **D. Obtener Análisis por ID**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/analisis/1`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```

---

### 📁 4. Análisis Directo (Sin Guardar)

#### 🔬 **A. Analizar URL Simple**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/phishing/analyze`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://amazon-security-alert.malicious.com/update"
  }
  ```
- **Descripción:** Analiza una URL sin guardarla en la BD (respuesta simplificada)

---

#### 🔬🔬 **B. Analizar URL Detallado**

- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/phishing/analyze-detailed`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer {{token}}
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://facebook-verify-identity.xyz/login"
  }
  ```
- **Descripción:** Análisis completo sin guardar

---

#### 💚 **C. Verificar Estado API ML**

- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/phishing/health`
- **Headers:**
  ```
  Authorization: Bearer {{token}}
  ```
- **Respuesta esperada:**
  ```json
  {
    "ml_api_available": true,
    "status": "OK"
  }
  ```

---

### 📁 5. API ML Directa (FastAPI)

#### 💚 **A. Health Check ML API**

- **Method:** `GET`
- **URL:** `{{mlApiUrl}}/health`
- **Headers:** _(ninguno)_

---

#### 🤖 **B. Predict URL (ML API Directa)**

- **Method:** `POST`
- **URL:** `{{mlApiUrl}}/predict-url-detailed`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "url": "https://paypal-verify.suspicious.com"
  }
  ```

---

## 🎬 FLUJO COMPLETO EN POSTMAN

### Secuencia Recomendada:

```
1️⃣ Registrar Usuario
   └─ POST /api/auth/registro

2️⃣ Login (Obtener Token)
   └─ POST /api/auth/login
   └─ El token se guarda automáticamente en {{token}}

3️⃣ Crear Enlace Sospechoso
   └─ POST /api/enlaces
   └─ El backend automáticamente:
      ✓ Guarda el enlace
      ✓ Llama a la API de ML
      ✓ Guarda el análisis
      ✓ Retorna todo junto

4️⃣ Ver el Análisis
   └─ GET /api/analisis/enlace/1/ultimo

5️⃣ Listar Enlaces del Usuario
   └─ GET /api/enlaces/usuario/1

6️⃣ Ver Todos los Phishing Detectados
   └─ GET /api/analisis/phishing
```

---

## 🧪 URLs de Prueba Recomendadas

### 🚨 URLs Sospechosas (Probablemente Phishing)

```json
// PayPal falso
{
  "url": "http://paypal-security-update.tk/verify-account",
  "aplicacion": "Email",
  "mensaje": "Solicita verificar cuenta urgente",
  "usuarioId": 1
}

// Banco falso con muchos dígitos
{
  "url": "https://secure-banking-login-12345.xyz/authenticate",
  "aplicacion": "SMS",
  "mensaje": "Mensaje de supuesto banco",
  "usuarioId": 1
}

// Microsoft falso
{
  "url": "http://microsoft-account-verify.info/secure/login.php",
  "aplicacion": "Email",
  "mensaje": "Verificación de cuenta Microsoft",
  "usuarioId": 1
}

// Amazon falso
{
  "url": "https://amazon-security-alert-verification.ml/update-payment",
  "aplicacion": "Email",
  "mensaje": "Alerta de seguridad de Amazon",
  "usuarioId": 1
}
```

### ✅ URLs Legítimas

```json
// Google
{
  "url": "https://www.google.com",
  "aplicacion": "Navegador",
  "mensaje": "Motor de búsqueda",
  "usuarioId": 1
}

// GitHub
{
  "url": "https://github.com/Jeanpierre-1/Backend_Phishing",
  "aplicacion": "Desarrollo",
  "mensaje": "Repositorio del proyecto",
  "usuarioId": 1
}

// Universidad
{
  "url": "https://www.universidad.edu.pe/portal/estudiantes",
  "aplicacion": "Email Institucional",
  "mensaje": "Portal de estudiantes",
  "usuarioId": 1
}
```

---

## 💡 Tips de Postman

### 1. **Auto-Guardar Token en Login**

En la petición de Login, pestaña **Tests**:

```javascript
var jsonData = pm.response.json();
pm.collectionVariables.set("token", jsonData.token);
console.log("✅ Token guardado automáticamente");
```

### 2. **Verificar Variables**

Haz clic en el ojo 👁️ en la esquina superior derecha para ver las variables actuales.

### 3. **Organizar por Colores**

- 🟢 Verde: Autenticación
- 🔵 Azul: Enlaces
- 🟡 Amarillo: Análisis
- 🟣 Morado: API ML

### 4. **Crear Entorno (Environment)**

Si usas múltiples servidores (desarrollo, producción):

- **Development**
  - `baseUrl`: `http://localhost:8080`
  - `mlApiUrl`: `http://localhost:8000`

- **Production**
  - `baseUrl`: `https://api.tudominio.com`
  - `mlApiUrl`: `https://ml.tudominio.com`

---

## 🎯 Checklist de Pruebas

- [ ] ✅ Registrar usuario exitosamente
- [ ] 🔑 Obtener token JWT (verificar en Variables)
- [ ] 🔗 Crear enlace sospechoso → Ver análisis en respuesta
- [ ] ✅ Crear enlace legítimo → Confirmar isPhishing = false
- [ ] 📋 Listar todos los enlaces
- [ ] 📊 Ver historial de análisis
- [ ] ⚠️ Listar solo phishing detectados
- [ ] 🔬 Analizar URL sin guardar
- [ ] 💚 Verificar health de API ML
- [ ] ✏️ Actualizar un enlace
- [ ] 🗑️ Eliminar un enlace

---

¡Ahora tienes todo listo para probar la API completa en Postman! 🚀
