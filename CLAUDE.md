# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- `./gradlew assembleDebug` — Build debug APK
- `./gradlew assembleRelease` — Build release APK
- `./gradlew test` — Run unit tests
- `./gradlew connectedAndroidTest` — Run instrumented tests (requires emulator/device)
- `./gradlew <module>:test --tests <ClassName>` — Run a single test class (e.g., `./gradlew :app:test --tests MainScreenViewModelTest`)
- `./gradlew :app:connectedAndroidTest --tests <ClassName>` — Run a single instrumented test
- `./gradlew lint` — Run lint analysis

## Architecture

**Navigation 3**: The app uses `androidx.navigation3` (not the older Fragment-based Navigation component). Navigation uses:
- `NavKey` — serializable keys that define destinations
- `NavBackStack` — holds the navigation stack
- `NavDisplay` — composable that renders the navigation state
- `entryProvider` — DSL to register destinations

**UI Layer**: MVVM with `ViewModel` + `StateFlow`
- ViewModels expose UI state via `StateFlow<UiState>`
- UI state uses sealed interfaces (e.g., `MainScreenUiState`: Loading, Error, Success)
- Compose screens collect state with `collectAsStateWithLifecycle()`

**Data Layer**: Repository pattern
- `DataRepository` interface defines data sources
- `DefaultDataRepository` provides hardcoded sample data
- Data flows as `Flow<List<String>>`

**Theme**: Compose Material3 with custom `MyApplicationTheme`
- Colors in `theme/Color.kt`
- Typography in `theme/Type.kt`
- Theme in `theme/Theme.kt`

## Project Structure

```
app/src/
├── main/java/com/example/someapp/
│   ├── MainActivity.kt           # Entry point
│   ├── Navigation.kt             # NavDisplay and routing
│   ├── NavigationKeys.kt        # NavKey definitions
│   ├── data/                     # Data layer
│   │   └── DataRepository.kt
│   ├── theme/                    # Compose theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── ui/main/                  # Main screen
│       ├── MainScreen.kt
│       └── MainScreenViewModel.kt
├── test/                         # Unit tests
└── androidTest/                  # Instrumented tests
```

## Key Dependencies

- Kotlin 2.3.20 with Compose compiler plugin
- Navigation 3 (runtime + ui)
- Lifecycle ViewModel for Navigation 3
- Compose BOM 2026.03.01

## Notes

- Uses version catalog (`gradle/libs.versions.toml`) for dependency management
- Edge-to-edge is enabled in `MainActivity`
- AIDL, buildConfig, and shaders are disabled in build config