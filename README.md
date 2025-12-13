# rhhub-app

Este repositorio contine un proyecto de Spring Boot v3 + Java v17 + MySQL + Vue 3


## 🏗️ Estructura del Proyecto

```
project-root/
├── pom.xml              # PADRE (packaging pom)
├── backend/
│   ├── pom.xml         # Spring Boot
│   └── src/
└── frontend/
    ├── pom.xml         # Vue 3 (controlado por Maven)
    ├── package.json
    └── vite.config.js
```
---

## 🛠️ **4️⃣ Cómo se Construye TODO**

📍 **Siempre desde `project-root`**
```bash
mvn clean install
```

**Orden real:**

1️⃣ **Frontend** → `npm run build` (crea `frontend/dist/`)

2️⃣ **Backend** → copia `dist/` a `classes/static/`  

3️⃣ **Spring Boot** → empaqueta JAR único  

---

## 📦 **Resultado Final**

```
backend/target/backend-0.0.1-SNAPSHOT.jar
 └── BOOT-INF/classes/static/
     ├── index.html          # ← Vue SPA
     ├── favicon.ico
     └── assets/
         ├── app-xxx.js
         └── app-xxx.css
```

**Ejecutas:**
```bash
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```

**Accedes:**
- 🌐 **Frontend:** `http://localhost:8080/`
- 🔧 **API Backend:** `http://localhost:8080/api/...`

---

## ⚡ **Comandos Rápidos**

```bash
# 📦 CONSTRUIR TODO
mvn clean install

# 🔍 VER qué hay en el JAR
jar tf backend/target/backend-0.0.1-SNAPSHOT.jar | findstr static

# 🚀 EJECUTAR
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar

# 🧪 SOLO backend (construye frontend primero)
mvn clean install -pl backend -am

# 🎯 SOLO frontend (para pruebas)
cd frontend
npm run build
```

---

## ✅ **Verificación Paso a Paso**

```bash
# 1. Limpiar
mvn clean

# 2. Construir frontend (debería crear dist/)
mvn install -pl frontend
# Verifica: ls frontend/dist/

# 3. Construir backend (copia y empaqueta)
mvn install -pl backend

# 4. Ejecutar
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```

---

## 📋 **Comandos para Verificar**

```bash
# 1. Valida estructura desde la raíz
cd C:\Users\victo\Desktop\WorkSpace\Proyectos\RHhub
mvn validate

# 2. Limpia cache
mvn clean

# 3. Construye TODO el proyecto
mvn clean install

# 4. Si solo quieres probar backend
mvn clean install -pl backend -am
```

## 🚀 **Ejecución del Proyecto Completo**

```bash
# Construir todo (backend + frontend)
mvn clean install

# Solo backend (sin frontend)
mvn clean install -pl backend

# Solo frontend (npm install + build)
mvn clean install -pl frontend

# Ejecutar backend Spring Boot
cd backend
mvn spring-boot:run

# Modo silencioso
mvn clean install -pl frontend -DskipTests -q  
mvn clean install -pl backend -DskipTests -q 
```