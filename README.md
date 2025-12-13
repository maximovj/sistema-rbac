# 🏗️ Estructura del Proyecto

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

# 1️⃣ POM PADRE (project-root/pom.xml)

👉 **Este es el ÚNICO pom con `<packaging>pom</packaging>`**
👉 **Aquí se definen módulos y versiones comunes**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <!-- ESTE es el parent de tu proyecto completo -->
    <groupId>com.github.maximovj</groupId>
    <artifactId>rhhub</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging> <!-- ✅ Aquí SÍ va packaging=pom -->
    
    <name>RHhub Fullstack Application</name>
    
    <modules>
        <module>backend</module>
        <module>frontend</module>
    </modules>
    
    <!-- Parent para Spring Boot (HEREDA todas las versiones) -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version> <!-- o 3.3.4, 3.2.7 según necesites -->
        <relativePath/> <!-- Busca en repositorios -->
    </parent>
    
    <properties>
        <java.version>17</java.version>
        <spring-restdocs.version>3.0.0</spring-restdocs.version>
        <frontend-maven-plugin.version>1.15.0</frontend-maven-plugin.version>
    </properties>
    
</project>
```

📌 **Aquí NO va Spring Boot como plugin ni dependencia directa**

---

# 2️⃣ Backend (`backend/pom.xml`)

⚠️ **Aquí SÍ cambia el `<parent>`**
❌ Ya NO apunta a `spring-boot-starter-parent`
✅ Ahora apunta al **POM PADRE**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <!-- Parent es el POM raíz -->
    <parent>
        <groupId>com.github.maximovj</groupId>
        <artifactId>rhhub</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath> <!-- Apunta al POM raíz -->
    </parent>
    
    <!-- Solo artifactId, NO grupo/versión (los hereda) -->
    <artifactId>backend</artifactId>
    <name>rhhub-backend</name>
    <packaging>jar</packaging> <!-- ✅ Aquí va packaging=jar -->
    
    <description>Backend Spring Boot para RHhub</description>
    
    <!-- NO necesitas <properties> ni <dependencies> con versiones -->
    <!-- Las versiones las maneja Spring Boot parent -->
    <dependencies>
        <!-- SIN versiones explícitas -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web-services</artifactId>
		</dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.restdocs</groupId>
			<artifactId>spring-restdocs-mockmvc</artifactId>
			<scope>test</scope>
		</dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Plugins SIN versiones (las maneja Spring Boot) -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            
            <!-- Plugin para copiar frontend -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-frontend</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.outputDirectory}/static</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>../frontend/dist</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

---

# 3️⃣ Frontend (`frontend/pom.xml`)

👉 Controla **Node + NPM + Vite SOLO con Maven**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.github.maximovj</groupId>
        <artifactId>rhhub</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    
    <artifactId>frontend</artifactId>
    <name>rhhub-frontend</name>
    <packaging>pom</packaging>
    
    <properties>
        <!-- 🔧 VERSIONES CORRECTAMENTE CERRADAS -->
        <frontend-maven-plugin.version>1.15.0</frontend-maven-plugin.version>
        <exec-maven-plugin.version>3.1.0</exec-maven-plugin.version>  <!-- ✅ CERRADO BIEN -->
        <antrun-maven-plugin.version>3.1.0</antrun-maven-plugin.version>
    </properties>
    
    <build>
        <plugins>
            <!-- 1. Plugin para detectar Node.js -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>${exec-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>detect-node</id>
                        <phase>initialize</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>cmd</executable>
                            <arguments>
                                <argument>/c</argument>
                                <argument>echo 📦 Node.js: &amp;&amp; node --version</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            
            <!-- 2. Mensaje informativo -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-antrun-plugin</artifactId>
                <version>${antrun-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>welcome-message</id>
                        <phase>initialize</phase>
                        <goals>
                            <goal>run</goal>
                        </goals>
                        <configuration>
                            <target>
                                <echo>=========================================</echo>
                                <echo>   🚀 CONSTRUYENDO FRONTEND (VUE 3)   </echo>
                                <echo>=========================================</echo>
                                <echo>✅ Node.js detectado en el sistema</echo>
                                <echo>✅ Usando instalación local</echo>
                                <echo>✅ Saltando descarga automática</echo>
                                <echo>=========================================</echo>
                            </target>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            
            <!-- 3. Plugin frontend (usa Node.js local) -->
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>${frontend-maven-plugin.version}</version>
                <configuration>
                    <skip>true</skip> <!-- No descargar Node.js -->
                    <workingDirectory>./</workingDirectory>
                </configuration>
                <executions>
                    <!-- Solo npm install -->
                    <execution>
                        <id>npm-install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    
                    <!-- npm run build -->
                    <execution>
                        <id>npm-build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <phase>prepare-package</phase>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

---

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