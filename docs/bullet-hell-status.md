# Undertale Bullet Hell Simulator — Status Document

**Path:** `src/alpha/Test.java`  
**Version header:** `@author air / Alpha 1.1`  
**Lines:** 2,356  
**Classes in file:** 18  
**Assessed:** 2026-06-15

## Repository Layout

```
Undertale-Simulators/
├── README.md
├── UndertaleSimulator.iml
├── .gitignore
├── imgs/gb.jpg
├── src/
│   ├── alpha/
│   │   ├── Test.java          # 2,356 lines – bullet hell, single file monolith
│   │   └── imgs/ 7 PNG sprites
│   ├── coloredtiles/Game.java # 298 lines – separate tiles game
│   └── prototype/Test.java    # 791 lines – early prototype
└── docs/                      # this folder
```

No build tool. IntelliJ module only. JDK Swing/AWT only dependency.

## Entry Point

`alpha.Test.main(String[])` → `new Test()`

Constructor in `Test` class 17-91:
* Loads 6 ImageIcons from hard path `src/alpha/imgs/...`
* Creates fullscreen JFrame via Toolkit screen size
* Instantiates static singletons on `Test` class
* Adds `GamePanel` JPanel, registers KeyListener, starts `new Thread(gp)`

All game state lives as `public static` fields on `Test`:

```
static JFrame frame
static CoordinateSystem cSystem
static Soul player
static GamePanel gp
static float ticks
static Vector<Bone> bones
static Vector<Spear> spears
static Vector<Platform> platforms
static Vector<Warning> warnings
static int deltaX, deltaY
static boolean paused, over, win, restart
static Teleporter[] bounds
static Bound[] moveBorder
static Thread t
static final int fps=60
static ImageIcon soulRed, soulBlue_*, soulGreen
```

## Class Inventory

| Class | Lines | Extends | Purpose |
|-------|-------|---------|---------|
| Test | 17-91 | — | bootstrap + global state |
| GamePanel | 93-1855 | JPanel implements Runnable KeyListener | 1,763 line God object: render, loop, input, physics, collision, scheduler, attacks |
| Entity | 1858-1905 | — | x y width height direction speed Rectangle hitbox, move(), updateHitbox() |
| Soul | 1907-1995 | Entity | player hp 92 hpMax 92 karma 0, up/down/left/right booleans, soulMode String, gDirection int 0-3, gSpeed float, speed int, invincibleFrames, lastX lastY, Shield shield |
| Bone | 1997-2037 | Entity | damage, speed 20, disappearX/Y, fadeOut, duration/maxDuration, color String White Blue Orange, move override, randomizeColor |
| Teleporter | 2039-2069 | Entity | boolean activated, transport() to opposite edge |
| CoordinateSystem | 2071-2144 | — | boolean activated, Vector FunctionAttack, createFunctionAttack random factory, checkIfPlayerHit, delayDecrease |
| FunctionAttack | 2146-2160 | — | delay 300, float[50] xs ys, active boolean, equation String |
| LinearFunction | 2162-2184 | FunctionAttack | k b calculateY |
| QuadraticFunction | 2186-2212 | FunctionAttack | a b c |
| ExponentialFunction | 2214-2234 | FunctionAttack | a pow |
| TrigFunction | 2236-2282 | FunctionAttack | type sin/cos amplitude frequency phaseShift hShift |
| Bound | 2284-2288 | Entity | wall segment |
| Spear | 2290-2324 | Entity | disappearX/Y damage 11 color Magenta Yellow move |
| Shield | 2326-2329 | Entity | boolean activated |
| Warning | 2331-2334 | Entity | duration maxDuration |
| GameTools | 2336-2350 | — | static getOppositeDirection |
| Platform | 2352-2356 | Entity | disappearX/Y stub |

No enums. Direction int 0 up 1 right 2 down 3 left. SoulMode String "Red" "Blue" "Green". Color String.

## GamePanel Responsibilities Breakdown

~1,763 lines split roughly:

* Rendering 93-328 paint() override: drawBound, drawSoul flicker, drawHP yellow+magenta karma bar, drawWarnings lerp, drawBones, drawSpear, drawGameStatus, drawWin Chinese text, drawCoordinateSystem axes + equation strings, draw function polyline, drawGravityDirection arrow, drawPlatform defined but never called.
* Game loop run() 332-398: while true, sleep 1000/60, player.saveLocation, moveThePlayer, checkTeleport, holdsPlayerInBounds if Red, gravity, updateHitbox, checkHit, moveAttacks, scheduleAttack, warning/bone duration subtract, checkBonesDisappear, karmaHpDecrease, ticks++, cSystem.delayDecrease, drainInvincibility, hp<=0 => over, repaint.
* Input 668-729 keyPressed keyReleased: arrows, P pause toggle, R restart, BACKSPACE ticks+=300 skip, F8 hp full heal.
* Physics 423-463 moveThePlayer ceil speed/2, 975-1017 gravity Blue mode gSpeed+=2.6 per tick position+=gSpeed/60, 423-436 holdsPlayerInBounds clamp Red.
* Collision 731-793 checkHit: bone intersect with color/movement logic, function hit halves hp, spear magenta hit or shield block, yellow spear damages through shield.
* HP Karma 640-665 karmaHpDecrease tiered drain 40→1 over variable ticks.
* Mode switches 933-973 bluetify redtify greentify: set soulMode, speed, shield, resize moveBorder.
* Attack scheduler 471-638 scheduleAttack: 35 time windows from tick 60 to 27000 mapping to 20 attack methods.
* Attack methods 1023-1854: boneShaft, crossBones, crossBonesHorizontal, boneSpike, boneRain, sniperBone, foldBones, boneLazer, laserTrap, gasterBlasters, augmentedGasterBlasters, blueBone, spearAttack x3, platformOne, healPlayer, plus helpers.

## Attack Timeline

Ticks at 60 fps. 27,000 ticks = 7.5 min scripted then idle. Win flag never set.

| Ticks | Sec | Action |
|-------|-----|--------|
|60-120|1-2|boneSpike 2|
|120-300|2-5|redtify|
|300-600|5-10|boneShaft|
|600-900|10-15|heal 16|
|900-1500|15-25|crossBones|
|1500-1800|25-30|heal 20|
|1800-2760|30-46|boneSpike random|
|2760-3060|46-51|heal 20 redtify|
|3060-3690|51-61|boneRain 1|
|3690-3900|61-65|heal 20|
|3900-4200|65-70|boneRain player dir|
|4200-4500|70-75|heal 20|
|4500-4800|75-80|boneRain 3|
|4800-5100|80-85|heal 20|
|5100-5400|85-90|boneRain opposite|
|5400-5700|90-95|heal 20|
|5700-6000|95-100|boneRain 0|
|6000-6300|100-105|heal 20|
|6300-7200|105-120|bluetify random + sniper 90|
|7200-7800|120-130|heal 20 redtify|
|7800-9000|130-150|foldBones|
|9000-9600|150-160|heal 12|
|9600-10500|160-175|teleporters on boneLazer|
|10500-10800|175-180|teleporters off heal 7|
|10800-12000|180-200|teleporters on laserTrap|
|12000-12600|200-210|clear heal 7|
|12600-12900|210-215|gasterBlasters sniper|
|12900-13200|215-220|sniper random|
|13200-13500|220-225|heal 12|
|13500-14000|225-233|boneRain 2 sniper 180|
|14000-14300|233-238|heal 3|
|14300-14900|238-248|foldBones 0 boneShaft|
|14900-15200|248-253|heal 7|
|15200-15680|253-261|boneRain random|
|15680-16000|261-267|heal 8 redtify|
|16000-19600|267-327|coordinate system on function attacks|
|19600-20000|327-333|coord off heal 12|
|20000-20600|333-343|augmented GB + sniper 60|
|20600-21000|343-350|hp = hpMax|
|21000-21300|350-355|bluetify 2 crossBonesHorizontal|
|21300-22000|355-367|heal 13|
|22000-22420|367-374|bluetify 2 blueBone|
|22420-23020|374-384|heal 16|
|23020-23620|384-394|spearAttack|
|23620-23920|394-399|heal 12|
|23920-25000|399-417|spearAttackTwo|
|25000-25400|417-423|heal 20|
|25400-26000|423-433|spearAttackThree|
|26000-26400|433-440|heal 20 bluetify 2|
|26400-27000|440-450|platformOne|

## Assets

* `src/alpha/imgs/soul_red.png`, `soul_blue_up/down/left/right.png`, `soul_green.png`, `Bone.png` (Bone.png unused in code – bones drawn as rects)
* Top `imgs/gb.jpg` unused
* Fonts: "Monster Friend Back", "Determination Sans", "宋体" – likely missing on Linux, fallback expected.
* Chinese comments ~722 CJK chars, many garbled mojibake indicating GBK vs UTF-8 mismatch.
* No audio.

## Technical Debt List

Architecture:
* 1 file 2,356 lines, God object GamePanel 1,763 lines.
* 16 static mutable globals on Test – untestable, singleton, no multiple instances.
* No package separation, no MVC, no interfaces.
* Vector legacy synchronized collection used for all entity lists.
* String == comparison instead of .equals in several hot paths (`soulMode == "Red"`).
* Magic numbers: 500 arena, 20 border, 92 hp, 60 fps, hard coded coordinates.
* No enums for Direction, SoulMode, ColorType.

Game loop:
* while(true) no shutdown.
* Thread.sleep fixed no delta time, drift.
* Game thread mutates state, repaint called off EDT – Swing threading violation.
* paint() overridden not paintComponent(), no super call.

Rendering:
* Hard coded deltaX deltaY computed once.
* drawPlatform defined never called.
* New Color and int[] allocations per frame per bone/function.

Logic bugs static analysis:
* `Test.win` never assigned true – win screen dead code.
* `platformOne` adds Platform but movePlatforms() and drawPlatform() never invoked in loop – dead feature.
* Vector removal during indexed for-loop without i-- risks skipping elements in checkBonesDisappear, checkHit, warningDurationSubtract.
* Teleporter transport hard codes 425 / 75 ignoring player size.
* CoordinateSystem.checkIfPlayerHit samples 25 x points only – misses between samples.
* FunctionAttack Vector grows unbounded until coordinate system off – memory leak risk if left on.
* ImageIcon paths relative to `src/alpha/imgs` – breaks from jar or different cwd.
* Karma caps at hp but visual bar can desync.
* Spear yellow logic damages through shield but removes spear anyway – unclear intent matches Undertale Green soul mechanics partially.
* Chinese comments garbled.

Testability:
* Zero tests. No test framework. UI blocks main.
* Logic inseparable from Swing.

Performance:
* Vector sync overhead, per-frame allocations, O(n) collision checks naive but n small so okay.

## Functional Status Estimate

Compiles on JDK 8+ Swing. Likely runs:
* Starts paused, P to start.
* Arrows move, P pause, R restart on game over, BACKSPACE skip, F8 heal.
* Fullscreen 500x500 arena centered.
* HP 92, karma drain works tiered.
* Red Blue Green modes exercised.
* Scripted attacks run ~7.5 min then idle no win.
* Game over on hp<=0 shows restart prompt.

## Metrics

* 2,356 LOC alpha, 298 coloredtiles, 791 prototype
* 18 classes alpha, ~80 methods
* 10 TODO comments
* 35 attack phases, ~100 tick conditionals
* 7 PNG assets
* 0 external dependencies beyond JDK

## Splitting Readiness

Good candidates for extraction: Entity hierarchy, FunctionAttack hierarchy, GameTools, Soul Bone Spear separate model classes.

Blockers: static global Test state, GamePanel God object mixing render/logic/input/schedule, hard-coded scheduleAttack if-else chain, rendering tied to model, resource path hard-coded, stringly typed enums.

Target after split: ~35-40 files across model, view, system, attack, render, util packages, zero static mutable globals, instance GameState passed via constructor, data-driven attack timeline, separated Renderer implementing paintComponent, InputHandler KeyListener, GameLoop Runnable, unit-testable collision and attack logic.
