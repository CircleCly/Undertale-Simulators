# Undertale Simulators

Swing/AWT Undertale fangames – bullet hell + colored tiles puzzle. JDK 8+, zero external dependencies.

## Quick Start

```bash
cd /data/repos/Undertale-Simulators
git checkout refactor/split-alpha
dotslash_javac -encoding UTF-8 -d bin $(find src -name "*.java")
dotslash_java -cp bin alpha.Test
```

Press **P** to unpause. Arrows to move. See **[docs/running.md](docs/running.md)** for full build/run instructions, controls, troubleshooting, and all three entry points.

## Games

| Package | Main Class | Description |
|---------|------------|-------------|
| `alpha` | `alpha.Test` | Bullet hell simulator – 7.5 min scripted attack timeline, Red/Blue/Green soul modes |
| `coloredtiles` | `coloredtiles.Game` | Colored tile puzzle |
| `prototype` | `prototype.Test` | Early bullet hell prototype |

## Docs

- [docs/running.md](docs/running.md) – How to build and run
- [docs/bullet-hell-status.md](docs/bullet-hell-status.md) – Current architecture / technical debt inventory
- [docs/splitting-plan.md](docs/splitting-plan.md) – Refactor plan (Phase 0–8, currently Phase 4 complete on `refactor/split-alpha`)

## IntelliJ

Open folder as project (`UndertaleSimulator.iml` checked in). Run configuration: Main class `alpha.Test`, Working directory = repository root (required for `src/alpha/imgs/...` asset loading).
