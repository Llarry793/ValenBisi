# ValenBisi 🚴

**Aplicación Android para consultar en tiempo real la disponibilidad de bicicletas y espacios libres en las estaciones de Valenbisi (Valencia, España).** La app carga el dataset oficial de Valenbisi, muestra todas las estaciones en una lista ordenable con indicador visual de disponibilidad y permite navegar a la ubicación exacta de cualquier estación en Google Maps.

***

## Descripción

ValenBisi consume el dataset público de disponibilidad de la red de bicicletas compartidas de Valencia (`valenbisi_disponibilitat`), lo parsea localmente desde un fichero JSON embebido y presenta la información en una lista interactiva. Con un solo tap el usuario accede al detalle completo de una estación y puede abrirla directamente en Google Maps usando las coordenadas GPS de la estación.

***

## Capturas de pantalla

| Lista de estaciones | Detalle de estación |
|:-------------------:|:-------------------:|
| ![Lista de estaciones](https://agi-prod-file-upload-public-main-use1.s3.amazonaws.com/48d0b5f7-71f8-4af8-b9f4-d9543966e4c3) | ![Detalle de estación](https://agi-prod-file-upload-public-main-use1.s3.amazonaws.com/268be0dc-61fe-4fac-b63a-d61654dfd67b) |

***

## Características principales

- 🗂️ **Lista de todas las estaciones**: muestra dirección, bicicletas disponibles y postes libres para cada estación de la red Valenbisi.
- 🟢🟡🔴 **Indicador visual de disponibilidad**: el texto de bicicletas disponibles cambia de color según el stock — verde (>10), amarillo (5-10) y rojo (<5).
- 🔃 **Ordenación flexible**: ordena la lista por dirección (A-Z) o por número de bicicletas disponibles (descendente) tanto desde botones en pantalla como desde el menú de la Toolbar.
- 📋 **Vista de detalle**: al pulsar una estación se abre una pantalla con toda su información: dirección, bicicletas disponibles, espacios libres, capacidad total y fecha/hora de la última actualización.
- 🗺️ **Abrir en Google Maps**: desde la pantalla de detalle, un botón FAB y una opción del menú permiten abrir la estación directamente en Google Maps con las coordenadas exactas.
- 📡 **Fuente de datos oficial**: los datos provienen del open data del Ayuntamiento de Valencia (fichero `valenbisi_disponibilitat.json`).

***

## Flujo de navegación

```
MainActivity
  └── Lista de estaciones (RecyclerView)
        ├── Botones: [Ordenar por dirección] [Ordenar por bicis]
        ├── Menú Toolbar: ordenar por nombre / por bicis
        └── [Tap en estación]
              └── DetallesActividad
                    ├── Información completa de la estación
                    ├── [FAB / Menú] ──► Google Maps (geo URI con lat/lon)
```

El objeto `EstacionBicis` se pasa entre Activities usando `Parcelable`, sin serialización adicional ni bundles complejos.

***

## Modelo de datos

### `EstacionBicis`
Data class que representa una estación de Valenbisi e implementa `Parcelable` para su paso entre Activities.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `direccionEstacion` | String | Dirección física de la estación |
| `numero` | Int | Número identificador de la estación |
| `operativa` | Boolean | Si la estación está abierta (`"T"` en el JSON) |
| `bicisDisponibles` | Int | Número de bicicletas disponibles para coger |
| `postesLibres` | Int | Número de postes libres para devolver |
| `totalPostes` | Int | Capacidad total de la estación |
| `ticket` | Boolean | Si dispone de máquina de tickets |
| `ultimaActualizacion` | String | Fecha y hora del último refresco del dato |
| `latitud` | Double | Coordenada GPS (latitud) |
| `longitud` | Double | Coordenada GPS (longitud) |

### Parseo del JSON
El fichero `res/raw/valenbisi_disponibilitat.json` se lee como `InputStream`, se convierte a `String` y se parsea con `org.json.JSONArray`. Por cada objeto del array se construye una instancia de `EstacionBicis` que se añade a la lista principal.

***

## Estructura del proyecto

| Clase | Tipo | Responsabilidad |
|-------|------|-----------------|
| `MainActivity` | Activity | Pantalla principal: carga el JSON, muestra la lista y gestiona la ordenación |
| `DetallesActividad` | Activity | Pantalla de detalle de una estación con navegación a Google Maps |
| `EstacionBicisAux` | RecyclerView.Adapter | Renderiza cada estación en la lista con indicador de color por disponibilidad |
| `EstacionBicis` | Data class (Parcelable) | Modelo de dominio con todos los campos de una estación Valenbisi |

***

## Tecnologías utilizadas

- **Kotlin** — lenguaje principal de toda la aplicación.
- **RecyclerView** — lista eficiente con ViewHolder para renderizar las estaciones.
- **org.json (JSONArray / JSONObject)** — parseo nativo del fichero JSON embebido sin dependencias externas.
- **Parcelable** — serialización eficiente del modelo `EstacionBicis` para su paso entre Activities.
- **Google Maps (Intent geo URI)** — navegación a la ubicación de cada estación usando la app de mapas instalada, sin necesidad del SDK de Maps.
- **Material Design Components** — Toolbar con menú de opciones y FloatingActionButton.
- **Open Data Valencia** — dataset público `valenbisi_disponibilitat` del Ayuntamiento de Valencia.
- **Android Studio** — entorno de desarrollo oficial para Android.

***

## Origen de los datos

Los datos utilizados provienen del **Portal de Datos Abiertos del Ayuntamiento de Valencia**:

- **Dataset**: Valenbisi Disponibilitat
- **URL**: [https://valencia.opendatasoft.com](https://valencia.opendatasoft.com)
- **Formato**: JSON
- **Actualización**: en tiempo real (la app usa un snapshot embebido en `res/raw/`)

***

## Requisitos

- Android 6.0 (API 23) o superior
- Google Maps instalado en el dispositivo (para la funcionalidad de navegación)
- Android Studio Hedgehog o posterior (para compilar desde código fuente)
- No se requiere conexión a internet (los datos están embebidos en la app)

***

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Llarry793/ValenBisi.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza las dependencias de Gradle.
4. Ejecuta la app en un emulador o dispositivo físico con Android 6.0+.

***

## Posibles mejoras futuras

- Sustituir el fichero JSON embebido por una **llamada en tiempo real** a la API del Open Data de Valencia para mostrar siempre datos actualizados.
- Añadir un **mapa interactivo** (SDK de Google Maps o OSMDroid) con marcadores para todas las estaciones.
- Implementar **búsqueda por dirección** para filtrar estaciones desde la lista.
- Añadir **favoritos** para guardar las estaciones más usadas.
- Implementar **widget de pantalla de inicio** que muestre la disponibilidad de una estación favorita.
- Migrar a arquitectura **MVVM con ViewModel + StateFlow** para separar mejor la lógica de la UI.

***

## Autores

Desarrollado por **Miguel** y **Óscar** (UV) como proyecto Android con foco en consumo de datos abiertos, parseo de JSON, RecyclerView y navegación entre Activities con Parcelable.
