# Repository Guidelines

## Project Structure & Module Organization
Djenni is a multi-module Maven project. `util` holds shared helpers; `core` is the primary runtime, exposing suppliers and builders under `core/src/main/java`. `common-generators` and `source-generator-mojo` provide reusable generators, while `djenni-source-maven-plugin` packages the Maven plugin consumed by downstream projects. Integration examples and fixtures live in `core-examples` and `djenni-source-maven-example`. Tests mirror source layouts under each module’s `src/test/java`, so add new tests beside the code they exercise.

## Build, Test, and Development Commands
Use `mvn clean verify` from the repo root for a full Java 21 build and test pass. Target a module with `mvn -pl core test` (replace `core` as needed) to speed up local loops. Build and install the plugin plus its prerequisites with `mvn -pl djenni-source-maven-plugin -am install`, which also compiles dependent modules (e.g., `util`, `core`).

## Coding Style & Naming Conventions
Follow the existing four-space indentation, brace-on-new-line Java style, and keep packages under `com.robertboothby.djenni`. Supplier implementations are nouns (`CachingSupplier`), while builders end with `SupplierBuilder`. Preserve `StreamableSupplier` immutability; builder classes remain mutable. Always compile with the `-parameters` flag (already configured in module POMs) so the dynamic builder can resolve names.

## Testing Guidelines
JUnit 4 with Mockito and Hamcrest is the standard; mirror the `ClassNameTest` naming seen in `core/src/test/java`. Cover new branches introduced by suppliers, especially concurrency or `ThreadLocal` paths. Property-driven scenarios should use randomized supplier inputs to prove irrelevance assumptions. Run `mvn test` (or module-targeted variants) before sending a pull request, and add regression tests whenever bugs are fixed.

## Commit & Pull Request Guidelines
Recent history shows concise, sentence-style commit subjects (e.g., “Updated all project dependencies and tweaked readme.md to improve language”). Match that tone: describe what changed and why in one line, elaborate in the body if needed. Pull requests should link any GitHub issues, summarize affected modules, note build/test commands executed, and include screenshots or snippets when altering docs or generator outputs. Highlight any required configuration (`-parameters`, plugin setup) so reviewers can reproduce the change quickly.

## Configuration Tips
Keep ~/.m2 settings pointing at OSSRH if you intend to release; snapshots publish through the provided `ossrh` profile. IntelliJ users should mirror Maven’s `-parameters` flag under `Build, Execution, Deployment > Compiler` to avoid reflection failures during plugin runs.
