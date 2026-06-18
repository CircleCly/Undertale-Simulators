# How to Run Undertale Simulators

## Prerequisites

- JDK 8+ (Swing/AWT only, no external dependencies)
- Linux/macOS terminal with X11/Wayland display for the Swing UI

At Meta, use the dotslash wrappers:

```
dotslash_javac -version
dotslash_java -version
```

Outside Meta, plain `javac` / `java` work identically.

## Repository / Branch

```bash
cd /data/repos/Undertale-Simulators
git checkout refactor/split-alpha   # Phase 4 complete – split into ~40 files
```

`main` still contains the original monolithic `src/alpha/Test.java` (2,356 lines).

## Build

From the repository root (required – image assets are loaded via `src/alpha/imgs/...` relative paths):

```bash
rm -rf bin
mkdir -p bin
dotslash_javac -encoding UTF-8 -d bin $(find src -name "*.java")
```

Notes:
- `-encoding UTF-8` is required – source contains CJK comments originally in GBK, now converted.
- Output classes go to `bin/`.
- No build tool (Maven/Gradle) yet – Phase 7/8 in `docs/splitting-plan.md`.

## Run

All commands must be run from the repository root so `src/alpha/imgs/*.png` resolves.

### Bullet Hell Simulator (alpha) – main game

```bash
dotslash_java -cp bin alpha.Test
```

Current entry point: `alpha.Test.main`
Future entry point (after Phase 6): `alpha.Main`

A fullscreen JFrame opens with a 500×500 arena centered on screen.

Controls:
| Key | Action |
|-----|--------|
| Arrow keys | Move soul |
| P | Pause / unpause |
| R | Restart (only on Game Over screen) |
| BACKSPACE | Skip ahead +300 ticks (~5 sec) |
| F8 | Full heal (cheat) |

Gameplay:
- Starts **paused** – press P to start
- HP: 92, Karma (purple) drains over time after taking damage
- Soul modes: Red (free 4-way), Blue (gravity, arrow keys change gravity direction), Green (shield, blocks magenta spears)
- Scripted attack timeline: ~7.5 min / 27,000 ticks, then idle. No win condition yet (`Test.win` is never set).
- Game over on HP ≤ 0, shows restart prompt

Assets loaded at startup:
```
src/alpha/imgs/soul_red.png
src/alpha/imgs/soul_blue_up.png
src/alpha/imgs/soul_blue_down.png
src/alpha/imgs/soul_blue_left.png
src/alpha/imgs/soul_blue_right.png
src/alpha/imgs/soul_green.png
```
`Bone.png` exists but is unused – bones are drawn as colored rectangles.

### Colored Tiles Puzzle (coloredtiles)

```bash
dotslash_java -cp bin coloredtiles.Game
```

Arrow keys to move, R to regenerate puzzle. Separate from the bullet hell game.

### Prototype (prototype)

```bash
dotslash_java -cp bin prototype.Test
```

Early bullet hell prototype – 791 lines, single file.

## Troubleshooting

| Symptom | Cause / Fix |
|---------|-------------|
| `ImageIcon` shows blank / soul invisible | Run from repo root – `SpriteLoader` loads `src/alpha/imgs/...` relative to CWD. `cd /data/repos/Undertale-Simulators` first. |
| Garbled comments / compile error about unmappable character | Add `-encoding UTF-8` to `javac`. |
| Missing fonts "Monster Friend Back", "Determination Sans", "宋体" | Expected on Linux – Swing falls back to SansSerif automatically (`FontRegistry` handles this in Phase 4+). |
| `ClassNotFoundException: alpha.Test` | Classpath missing – use `-cp bin`, ensure `bin/alpha/Test.class` exists after compile. |
| Window opens off-screen / wrong size | Game uses `Toolkit.getDefaultToolkit().getScreenSize()` for fullscreen. Check display scaling. |
| `ConcurrentModificationException` during paint | Known risk pre-Phase 7 – Vector → ArrayList migration with snapshot copy is planned. Current `refactor/split-alpha` still uses `Vector`. |

## IDE

IntelliJ: Open folder as project, `UndertaleSimulator.iml` is checked in. Run configuration: Main class `alpha.Test`, Working directory = repository root.

## Current Refactor Status

Branch `refactor/split-alpha` – Phase 4 complete:
- Model classes extracted to `alpha.model.*`
- Enums: `Direction`, `SoulMode`, `BoneColor`, `SpearColor`
- Systems: `MovementSystem`, `PhysicsSystem`, `CollisionSystem`, `KarmaSystem`, `LifetimeSystem`, `ModeSystem`
- Render split: `alpha.render.Renderer`, `SpriteLoader`, `FontRegistry`
- Input split: `alpha.input.InputHandler`
- Attacks extracted to `alpha.attack.*` classes, but `AttackScheduler` still uses the old if-else chain (data-driven timeline is Phase 5)

Static mutable globals still live on `Test.STATE` – elimination is Phase 6.

See `docs/splitting-plan.md` and `docs/bullet-hell-status.md` for full architecture notes.
