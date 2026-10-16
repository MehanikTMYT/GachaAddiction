# NeoForge 26.1.2 port — updated 2026-10-09

Work in progress. All original integration sources are compiled and packaged; this is not yet a fully gameplay-tested release with all integrations.

## Upstream check

Fetched the fork and [original repository](https://github.com/Wang-Xiao-Jing/GachaAddiction) before continuing. Neither main branch has new commits relative to `786f920b346f2780c7b1887debeee9d407042ae1` (2026-09-14). Upstream also has `fabric-26.2` at `44e1cdf`, but no NeoForge 26.1.2 port was found. This port keeps the main branch's integrations instead of replacing it with the smaller Fabric integration set.

## Current implementation

- Minecraft 26.1.2, NeoForge 26.1.2.100, Java 25, ModDevGradle 2.0.147.
- Updated identifiers, registry lookups, networking codecs, command permissions, persistence and GUI extraction APIs.
- Adapted chest/minecart menu data transfer and corrected minecart save/load callbacks.
- Updated Lootr inventory/store APIs; restore per-player opening state from Lootr's persisted actual-openers set.
- Updated FTB JSON/config APIs and client reward callback signature.
- Optional integration mixins are selected by installed mod ID; client-only mixins are in the client section.
- Preserved the shared reel host used by both the main screen and LDLib2 preview.
- Regenerated English, Russian and Chinese translations and sound definitions; added missing reward sound subtitles.

## Dependency ports (2026-10-09)

Local source ports now exist in `../gacha-deps/`: Treasure Bags, Loot Bundles, ColorTooltips, and the author's 26.1.2 Confluence Magic Lib branch. Build them with `bash ../gacha-deps/build.sh` before building Gacha. Full provenance, commands and remaining limitations are recorded in [the dependency status](../gacha-deps/PORT_STATUS.md).

KubeJS/Rhino, Better Advanced Tooltips (required by KubeJS), Silent Lib, Architectury, Shatterbyte/ShatterLib, FTB Filter System and FTB Ultimine now use available 26.1.2 releases. No 1.21.1 dependency binaries remain in the default development runtime.

Clavis remains unavailable: its project has no public source repository or 26.1.2 release found; the user confirmed ARR licensing. All requested integrations cannot be called complete while this remains unresolved.

## Development runtime profiles

Profiles change only development runtime dependencies; every Gacha integration source is compiled and packaged:

- `ported` (default): all available integrations and updated original development dependencies, including the four local builds; excludes unavailable Clavis.
- `supported`: the earlier LDLib2/Lootr/FTB/RarityCore/JEI/Jade set.
- `core`: skips optional runtime dependencies (Jade remains on the implementation classpath).
- `all`: fails explicitly with the Clavis availability message.

Use `-PgachaDepsRoot=/absolute/path` to select the local dependency checkout root. `stageIntegrationMods` stages Gacha and the selected mod JARs into `build/integration-mods/`; it does not install or publish them.

Normal commands, with Java 25 installed:

```sh
bash gradlew build
bash gradlew runClient
bash gradlew runServer
bash gradlew runData -PgachaRuntime=core
```

Run data generation before a separate build when updating translations: the datagen process itself needs resources, so `runData build` can package the preceding resource snapshot.

## Validation performed

- Build succeeded, compiling all integration sources. No automated unit tests exist (`test NO-SOURCE`).
- Core dedicated server reached `Done` and populated the loot-table cache.
- Supported-profile dedicated server reached `Done` with Lootr, FTB and LDLib2 loaded.
- Supported-profile client initialized mods, renderer, sounds and resource atlases under Xvfb without a mixin failure in the observed log. The timed run ended without gameplay/UI interaction; this is not confirmation of every screen working. Missing subtitle messages from that run were subsequently addressed in the translations.
- Datagen completed successfully for all three languages and sound definitions.
- `git diff --check` passed.

Local checks reused already prepared, matching NeoForge 26.1.2.100 Minecraft artifacts from the workspace's Create-Stock-Bridge build (no access transformer there). Commands therefore used `-x createMinecraftArtifacts --no-configuration-cache`; fresh artifact generation was not part of the successful checks. An earlier attempt to generate 26.1.2.109 artifacts was stopped due to resource use. The project is pinned to the tested 26.1.2.100 baseline.

Validation logs are saved locally under `build/port-validation/`. Dedicated server processes were deliberately stopped after `Done`; their resulting Gradle nonzero exit is not a startup failure. The client run was time-limited.

## Remaining work

- Obtain an available Clavis source port or official 26.1.2 build.
- Resolve missing optional scripting plugin classes reported by official KubeJS and LDLib2 builds; details are in the dependency status.
- Exercise chest and minecart opening, reward animation, LDLib2 configuration/preview, FTB quest rewards and loot crates in game.
- Verify Lootr first-open/reopen behavior for multiple players across a world restart, and check network synchronization with a remote client.
- Validate a clean artifact preparation/build and the final packaged JAR in a regular game installation.

Artifact: `build/libs/gachaaddiction-1.1.4+26.1.2.jar` (development build, not a release).
