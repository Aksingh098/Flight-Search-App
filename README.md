# Flight Search

An Android app for searching airports and browsing possible flight routes, built with Kotlin and Jetpack Compose. Users can search for an airport by name or IATA code, view all possible destination flights from the selected airport, and save favorite routes for quick access later.

## Features

- **Airport autosuggestion** — search by airport name or IATA code, with results ranked by passenger count and debounced as you type.
- **Flight route listing** — select an airport to see every possible destination flight from it.
- **Favorites** — mark/unmark routes as favorites; favorite status persists and is reflected instantly across the UI.
- **Offline-first** — all data is served from a local, pre-populated Room database (no network calls).

## Screenshots

| Search | Suggestions | Flight Results |
|---|---|---|
| <img src="https://github.com/user-attachments/assets/9349afb0-fb20-4b91-a7e6-c3197ce475a3" width="220"> | <img src="https://github.com/user-attachments/assets/6bf38838-a960-4430-ae86-817e6d7e1039" width="220"> | <img src="https://github.com/user-attachments/assets/871ee37d-0911-446a-a0f5-f544624dcc63" width="220"> |
## Tech Stack

- **Kotlin**
- **Jetpack Compose** with **Material 3** (`SearchBar`, `ElevatedCard`, edge-to-edge)
- **Room** for local persistence, seeded from a bundled `.db` asset
- **Kotlin Coroutines & Flow** — `StateFlow`, `flatMapLatest`, `combine`, and `debounce` drive reactive UI state
- **ViewModel** with a manual `AppContainer`-based dependency graph (no DI framework)
- **KSP** for Room's annotation processing

## Architecture

The app follows a layered architecture:

```
ui/            Composables + ViewModel (UI state, one-way data flow)
domain/        Plain Kotlin models (Airport, FlightRoute, Favorite)
data/
  repository/  FSearchRepository interface + OfflineFSearchRepository impl
  local/       Room entities, DAO, database, and AppContainer (manual DI)
```

**Data flow:**
1. `FSearchDao` exposes `Flow`-based queries against the `airport` and `favorite` tables.
2. `OfflineFSearchRepository` wraps the DAO behind the `FSearchRepository` interface.
3. `FlightSearchViewModel` combines the search query, expansion state, airport suggestions, selected-airport flight results, and favorites into a single `FlightSearchUiState` exposed as a `StateFlow`.
4. Composables in `FlightSearchScreen.kt` render that state and forward user actions (search, select airport, toggle favorite) back to the ViewModel.

Favorites are joined against flight results reactively — toggling a favorite updates the `isFavorite` flag on the corresponding route without a manual re-fetch.

## Project Structure

```
app/src/main/java/com/example/flightsearch/
├── FSearchApplication.kt       # Application class, initializes AppContainer
├── MainActivity.kt             # Single activity, hosts Compose content
├── data/
│   ├── Mapper.kt                # Entity → domain model mappers
│   ├── local/                   # Room entities, DAO, database, DI container
│   └── repository/              # Repository interface + offline implementation
├── domain/
│   └── Airport.kt                # Airport, FlightRoute, Favorite models
└── ui/
    ├── FlightSearchApp.kt         # Top-level Scaffold + TopAppBar
    ├── FlightSearchScreen.kt      # Search bar, suggestions list, results list
    ├── FlightSearchUiState.kt     # UI state data class
    ├── FlightSearchViewModel.kt   # State management + business logic
    └── theme/                     # Material3 theme, color, and typography
```

## Requirements

- Android Studio (current stable)
- JDK 21 (via Gradle toolchain)
- Min SDK 24, Target/Compile SDK 36/37

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio and let Gradle sync (dependencies are resolved via `gradle/libs.versions.toml`).
3. Run the app on an emulator or device (min API 24). The airport database is bundled as an asset and copied in automatically on first launch — no setup required.

## Notes

- The database is read-only and pre-seeded (`createFromAsset`); there's no UI for adding new airports.
- `fallbackToDestructiveMigration` is disabled, so schema changes will require a proper migration.
