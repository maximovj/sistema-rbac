# Sistema RBAC

Este repositorio contine un proyecto de Spring Boot v3 + Java v17 + MySQL + Vue 3

Este es un sistema de seguridad por roles (RBAC) cada usuario tiene un rol, y cada rol tiene permisos para activar o desactivar funcionalidades.

## ⚙️ Requisitos

- java >= v17
- springboot >= v3.4.0
- maven >= v3.8.9
- mysql >= 8.2
- node >= v20.20.2
- vite >= v7.2.7
- vue >= 3.5.25

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

## ✨ Ejecutar proyecto

**Paso 1)** Crear y configurar variables de entorno .env tanto en la carpeta backend, como en la carpeta frontend. 

Crear un copia del archivo .env.example por .env

**Paso 2)** Crear / Genererar ejecutables para el backend y frontend desde la carpeta raíz

```shell
$ maven clean install
```

**Paso 3)** Ejecutar / Levantar el proyecto para el backend y frontend desde la carpeta correspondiente

Backend 
```shell
$ cd backend
$ ./mvnw clean package (Opcional en Linux/MacOs)
# o
$ ./mvnw.cmd clean package (Opcional en Windows)
$ mvn spring-boot:run
```

FrontEnd
```shell
$ cd frontend
$ mvn clean install (Opcional en Windows/Linux/MacOs)
$ npm run dev
```

## 📦 **Resultado Final**

```
project-root/
├── pom.xml
├── backend/
│   ├── pom.xml
│   ├── target/backend-0.0.1-SNAPSHOT.jar       # Ejecutable jar
│   └── src/
└── frontend/
    ├── pom.xml
    ├── package.json
    ├── dist/index.html                         # Compilación Vue + Vite 
    └── vite.config.js
```

**Accedes:**

- 🌐 **Frontend:** `http://localhost:5173/`

- 🔧 **API Backend:** `http://localhost:8667/api/v1/...`

## 📷 Vistas Previas

![preview_01.png](/screenshots/preview_01.png)