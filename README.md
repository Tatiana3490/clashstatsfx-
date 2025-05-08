# ClashStatsFX

Aplicación JavaFX desarrollada como parte de la actividad de Programación de Servicios y Procesos (2ª Evaluación). Permite consultar información del juego Clash Royale mediante llamadas a su API oficial, aplicando principios de programación reactiva.

##  Tecnologías usadas

* Java 20
* JavaFX (Controles + FXML)
* RxJava 3
* Retrofit 2.11.0
* Gson 2.11.0
* Maven
* GitHub (control de versiones)

##  Requisitos obligatorios cumplidos

| Requisito                                             
| ----------------------------------------------------- 
| Uso de RxJava (programación reactiva)                 
| Al menos 2 endpoints diferentes                       
| Mostrar información detallada usando `ObservableList` 
| Uso de `TableView` en la interfaz                    
| Carga en segundo plano                            
| Búsqueda en datos                                     
| Filtro sobre los datos cargados                       

## Funcionalidades opcionales completadas

| Funcionalidad                                    
| ------------------------------------------------ 
| Mostrar contenido gráfico (iconos de cartas)     
| Exportación a CSV                               
| Compresión ZIP asíncrona con `CompletableFuture` 
| Uso de GitHub con ramas y issues                 

## Estructura del proyecto

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.svalero.clashstatsfx
│   │   │       ├── MainApp.java
│   │   │       ├── controller
│   │   │       │   └── MainController.java
│   │   │       ├── model
│   │   │       │   ├── BattleLogEntry.java
│   │   │       │   ├── Card.java
│   │   │       │   └── CardResponse.java
│   │   │       ├── service
│   │   │       │   ├── ClashRoyaleApi.java
│   │   │       │   └── RetrofitClient.java
│   │   │       └── utils
│   │   │           ├── CsvExporter.java
│   │   │           ├── ZipExporter.java
│   │   │           └── TokenManager.java
│   │   └── resources
│   │       ├── main-view.fxml
│   │       └── config.properties
└── pom.xml
```

##  Uso de la aplicación

1. Introducir el tag de jugador y presionar **"Cargar Batallas"**.
2. Usar el buscador para filtrar cartas por nombre.
3. Exportar la información cargada en CSV + ZIP.

##  Configuración del token de API

La aplicación necesita un token de autenticación para acceder a la API de Clash Royale.

1. Crea un archivo llamado `config.properties` dentro de `src/main/resources/`.
2. Dentro, añade esta línea:

   ```properties
   clash.token=Bearer TU_TOKEN_PERSONAL_DE_LA_API
   ```
3. Guarda el archivo. Está incluido en `.gitignore` para evitar subirlo por accidente.

##  Ejecución

```bash
mvn clean javafx:run
```

## Licencia

Este proyecto es parte de una actividad académica. Uso educativo y no comercial.

---

