# Bullet Hell Simulator — Complete Splitting Plan

**Target source:** `src/alpha/Test.java` 2,356 lines, 18 classes  
**Goal:** Extract into maintainable multi-file Java project preserving exact behavior, no static globals, testable logic, separated render vs model vs systems.  
**Output location in repo:** this plan lives at `docs/splitting-plan.md`. Implementation will create new source tree under `src/alpha/` with subpackages.

---

## 0. Principles

1. **Behavior preservation first.** No gameplay change in Phase 1-4. Visual output, tick timing, damage numbers, attack patterns identical.
2. **Static elimination.** Replace `Test.* static` with instance `GameState` owned by `Game` and passed explicitly.
3. **One class per file.** No nested top-level classes except maybe small enums in same file initially then split.
4. **Package by layer then feature.** `model`, `view`, `system`, `attack`, `render`, `input`, `util`, `resource`.
5. **No new dependencies in Phase 1-4.** Stay JDK Swing only. Phase 5 may add JUnit.
6. **Incremental commits.** Each step below is a separate git commit with compile + manual run verification. No massive rewrite commit.
7. **String → Enum.** Direction, SoulMode, BoneColor, SpearColor become enums with safe migration wrappers.
8. **Vector → ArrayList or CopyOnWrite?** Game is single-threaded game loop thread + EDT read-only during paint via snapshot copy. We'll switch to ArrayList and synchronize on GameState lock or copy-on-read for paint to avoid ConcurrentModification. Phase 2 decides.
9. **Resource loading abstraction.** Replace hard `"src/alpha/imgs/..."` with classpath resource loader falling back to file path for dev compatibility.

---

## 1. Target Package Structure

```
src/alpha/
  Main.java
  Game.java
  GameState.java
  GamePanel.java            // view only after split, later renamed to GameView
  input/
    InputHandler.java
    InputState.java
  model/
    Entity.java
    Soul.java
    Bone.java
    Spear.java
    Platform.java
    Teleporter.java
    Bound.java
    Shield.java
    Warning.java
    CoordinateSystem.java
  model/enums/
    Direction.java
    SoulMode.java
    BoneColor.java
    SpearColor.java
  attack/
    AttackScheduler.java
    AttackContext.java
    Attack.java             // interface
    HealPlayer.java
    ModeSwitch.java
    BoneShaftAttack.java
    CrossBonesAttack.java
    CrossBonesHorizontalAttack.java
    BoneSpikeAttack.java
    BoneRainAttack.java
    SniperBoneAttack.java
    FoldBonesAttack.java
    BoneLazerAttack.java
    LaserTrapAttack.java
    GasterBlastersAttack.java
    AugmentedGasterBlastersAttack.java
    BlueBoneAttack.java
    SpearAttack.java
    SpearAttackTwo.java
    SpearAttackThree.java
    PlatformOneAttack.java
    FunctionAttackSpawner.java
    TeleporterToggle.java
    CoordinateSystemToggle.java
  attack/function/
    FunctionAttack.java
    LinearFunction.java
    QuadraticFunction.java
    ExponentialFunction.java
    TrigFunction.java
  system/
    PhysicsSystem.java
    CollisionSystem.java
    KarmaSystem.java
    MovementSystem.java
    LifetimeSystem.java
    TeleportSystem.java
  render/
    Renderer.java
    SpriteLoader.java
    FontRegistry.java
  util/
    GameTools.java
    RectUtil.java
  resource/
    ResourceLoader.java
```

Approx 45-50 new files from 1.

Old `src/alpha/Test.java` deleted at end of Phase 4, replaced by `Main.java` entry point preserving `alpha.Test` as deprecated shim for one commit then removed, or keep Test as forwarding main for backward compatibility then remove in Phase 5.

---

## 2. Phase Overview

| Phase | Name | Goal | Files touched | Risk |
|-------|------|------|---------------|------|
|0|Repo hygiene|Create docs, .gitignore update, backup branch|docs/, .gitignore|low|
|1|Enums and Model extraction|Create enums, extract Entity hierarchy to separate files, no behavior change, still static globals|new model/, model/enums/|medium – string == to enum migration|
|2|GameState container|Introduce GameState instance to hold former Test static fields, migrate Test to delegate to singleton GameState instance still static accessors for compatibility|GameState.java, Test.java modified|high – touches every reference|
|3|System extraction|Pull out Physics, Collision, Karma, Lifetime, Movement, Teleport as stateless system classes operating on GameState|system/|medium|
|4|Renderer and Input split|Extract Renderer from GamePanel paint, extract InputHandler from KeyListener, GamePanel becomes thin view+loop orchestrator|render/, input/, GamePanel slim|medium|
|5|Attack Scheduler data-driven|Convert scheduleAttack 167-line if-else to AttackTimeline list of timed Attack objects, extract each attack method to its own Attack implementation class|attack/|high – behavior preservation critical|
|6|Game Loop and Main split|Extract Game.java loop runnable, Main.java bootstrap JFrame, GamePanel becomes pure JPanel view with paintComponent, remove static Test|Main.java Game.java GamePanel refactored Test.java deleted|high|
|7|Resource loader and cleanup|Classpath resource loading, fix encoding of comments to UTF-8, Vector to ArrayList, remove dead code platform rendering, add win condition hook, finalize package-private visibility|resource/, util/, various|low-medium|
|8|Optional test harness|JUnit 5 add, unit tests for collision, karma, function attack hit detection, attack timeline parsing|test/|low|

Phases 0-4 preserve monolithic scheduleAttack inside GamePanel but moved systems out. Phase 5 is biggest behavior-preserving refactor. Phase 6 eliminates static globals completely.

---

## 3. Detailed Step Plan

### Phase 0 — Repo hygiene [DONE partially]

* Create `docs/` folder. Add `bullet-hell-status.md` and `splitting-plan.md`. Done.
* Add to `.gitignore`: `bin/`, `out/`, `*.class`, `.DS_Store`, maybe `docs/` stays tracked.
* Create git branch `refactor/split-alpha`.
* Commit: "docs: add bullet hell status and splitting plan"

Verification: `git status`, no java changes.

---

### Phase 1 — Enums and Model extraction

**1.1 Create enums**

Files to create:
* `src/alpha/model/enums/Direction.java`
```java
package alpha.model.enums;
public enum Direction { UP(0), RIGHT(1), DOWN(2), LEFT(3); ... fromInt toInt opposite }
```
* `SoulMode.java` enum RED BLUE GREEN
* `BoneColor.java` WHITE BLUE ORANGE
* `SpearColor.java` MAGENTA YELLOW

Provide `fromString` and `toString` for backward compat during migration, and `fromInt` mapping preserving 0-3.

**1.2 Extract Entity base**

* Create `src/alpha/model/Entity.java` move class verbatim, change package to `alpha.model`, make public.
* Update imports in Test.java to import alpha.model.Entity or use fully qualified temporarily.

Repeat for each model class in dependency order:
1. `Bound.java` extends Entity – simple
2. `Shield.java`
3. `Warning.java`
4. `Platform.java`
5. `Teleporter.java` – depends on Test.player static for transport() – keep static reference for now, add TODO comment to inject GameState later in Phase 2.
6. `Bone.java`
7. `Spear.java`
8. `Soul.java` – depends on Shield, Test.player static self reference in directShield – keep static for now.
9. `CoordinateSystem.java` – depends on Test ticks and Test.player and Test.cSystem – keep static.
10. Function hierarchy to `attack/function/` package: FunctionAttack, LinearFunction, QuadraticFunction, ExponentialFunction, TrigFunction – keep package alpha.attack.function, update imports.

**1.3 Update Test.java**

* Keep classes as package-private stubs extending new public model classes for backward compatibility OR delete old inner classes and add imports. Prefer delete and import to force compilation errors guiding fixes.
* Replace all `String soulMode == "Red"` with `soulMode.equals` as interim, then later enum compare. First commit keeps String but centralizes constants in SoulMode enum with toString.
* Replace direction int literals with Direction enum in new model classes but keep int fields for now with conversion helpers to minimize behavior change.

**Verification per sub-step:**
* `javac -cp src -d bin src/alpha/model/*.java src/alpha/model/enums/*.java src/alpha/attack/function/*.java src/alpha/Test.java` must compile.
* Run `java -cp bin alpha.Test` manual smoke: window opens, P to start, arrows move, no crash. No gameplay change expected.

**Commit granularity:** one commit per 2-3 extracted classes to keep bisectable. Suggested 6 commits in Phase 1.

---

### Phase 2 — GameState container

Goal: eliminate static fields on Test, centralize in instance.

**2.1 Create GameState.java**
```java
package alpha;
public class GameState {
  public JFrame frame;
  public CoordinateSystem cSystem;
  public Soul player;
  public GamePanel gp;
  public float ticks;
  public List<Bone> bones = new ArrayList<>();
  public List<Spear> spears = new ArrayList<>();
  public List<Platform> platforms = new ArrayList<>();
  public List<Warning> warnings = new ArrayList<>();
  public int deltaX, deltaY;
  public boolean paused, over, win, restart;
  public Teleporter[] bounds;
  public Bound[] moveBorder;
  public Thread gameThread;
  public static final int FPS=60;
  public ImageIcon soulRed...;
}
```

**2.2 Modify Test.java to hold single static GameState instance for transition**

* Add `public static GameState STATE = new GameState();`
* Create static getters delegating: keep existing `Test.player` etc as deprecated static accessors forwarding to STATE for minimal code churn initially, OR do mass replace `Test.` with `GameState.STATE.` via sed then gradually instance-ize.
* Better approach: mass replace to `Test.STATE.` then later pass GameState as parameter.

Use bash script to replace: `sed -i 's/Test\.player/Test.STATE.player/g' ...` across file but careful.

Alternative incremental: introduce GameState and modify Test constructor to populate STATE fields instead of static fields, keep static fields as aliases pointing to STATE fields via static initializer block assigning references? Can't alias primitives easily. Choose mass replace.

**2.3 Update all model classes to accept GameState parameter instead of accessing Test static**

For Phase 2 keep static access but via Test.STATE to centralize. Later Phase 6 remove static entirely.

Example Teleporter.transport() currently does `Test.player.y = 425`. Change to `Test.STATE.player.y = 425`.

Do global search replace:
* `Test\.frame` → `Test.STATE.frame`
* `Test\.player` → `Test.STATE.player`
* etc for all 16 fields.

**Verification:**
* Compile same command but add new source root.
* Run manual smoke same as Phase1. Behavior identical.

**Commit:** "refactor: introduce GameState container, migrate Test static fields"

---

### Phase 3 — System extraction

Create `src/alpha/system/` package with stateless utility classes taking GameState as parameter.

Extract methods from GamePanel in order:

**3.1 MovementSystem.java**
* Methods moved: `moveThePlayer()`, `moveAttacks()`, `movePlatforms()`, `movePlatform()`, `holdsPlayerInBounds()`
* Signature: `public static void update(GameState gs)` or instance with gs in constructor.
* GamePanel calls `MovementSystem.update(Test.STATE)`

**3.2 PhysicsSystem.java**
* `gravity()`, `checkTeleport()`, `setTeleportersState()`, `setCoordinateSystemState()`

**3.3 CollisionSystem.java**
* `checkHit()`, plus helper `checkIfPlayerHit` already in CoordinateSystem but move orchestration here.

**3.4 LifetimeSystem.java**
* `boneDurationSubtract()`, `warningDurationSubtract()`, `checkBonesDisappear()`, `drainInvincibility()`

**3.5 KarmaSystem.java**
* `karmaHpDecrease()`, `healPlayer(int interval)`

**3.6 ModeSystem.java or keep in Soul?**
* `redtify() bluetify() greentify() restoreInitialBorder()` move to `SoulModeSystem` operating on GameState.

Each extraction:
* Cut method from GamePanel, paste into new System class as static method taking GameState.
* Replace original GamePanel method body with delegation call to preserve call sites inside GamePanel initially.
* Later call sites can call System directly.

**Verification after each system:** compile + manual run 30 seconds, test P pause, arrows, take damage, heal phases visible, no crash.

**Commit per system:** 6 commits.

---

### Phase 4 — Renderer and Input split

**4.1 InputHandler.java** in `src/alpha/input/`
* Implements KeyListener.
* Holds reference to GameState and InputState booleans.
* Move keyPressed keyReleased keyTyped logic from GamePanel.
* GamePanel no longer implements KeyListener; Test constructor registers new InputHandler instead of gp.

Create `InputState.java` simple POJO booleans up down left right to decouple from Soul directly? Soul already has booleans; InputHandler can set Soul fields directly via GameState for now to minimize change, later refactor.

**4.2 Renderer.java** in `src/alpha/render/`
* Methods moved from GamePanel: `drawBound`, `drawSoul`, `drawHP`, `drawBones`, `drawPlatform` (currently unused but keep), `drawWarnings`, `drawStatus`, `drawGravityDirection`, `drawGameStatus`, `drawWin`, `drawCoordinateSystem`, `drawFunctionAttacks`, `drawSpear`.
* Signature `public void render(Graphics g, GameState gs, JPanel panel)` where panel needed for ImageObserver in drawImage.
* GamePanel.paint(Graphics g) becomes thin:
```java
public void paint(Graphics g){
  renderer.render(g, Test.STATE, this);
}
```
Later change to paintComponent.

**4.3 SpriteLoader.java**
* Move ImageIcon loading from Test constructor to SpriteLoader.load().
* Test constructor calls SpriteLoader and assigns to GameState fields.

**4.4 FontRegistry.java**
* Centralize Font creation with fallback handling to avoid missing font crash. Try "Monster Friend Back" then fallback to SansSerif.

**Verification:** compile, run, visual parity check: soul renders, HP bar, bones gray/blue/orange, warnings color lerp, function red polyline, gravity arrow, pause text, win text never shown as before.

**Commit:** 2-3 commits for input, renderer, sprite loader.

---

### Phase 5 — Attack Scheduler data-driven

This is largest logic refactor but behavior-preserving.

Current `scheduleAttack()` is 167-line if-else chain hard coding tick ranges to method calls. We'll convert to timeline data structure.

**5.1 Create Attack interface**
```java
package alpha.attack;
public interface Attack { void tick(GameState gs, long tick); default void start(GameState gs){} default void end(GameState gs){} }
```

**5.2 Create AttackContext to hold shared helpers**
* healPlayer, redtify etc become reusable actions or part of ModeSwitchAttack.

**5.3 Extract each attack method from GamePanel into its own class implementing Attack**

List to create in `src/alpha/attack/`:
* BoneShaftAttack
* CrossBonesAttack
* CrossBonesHorizontalAttack
* BoneSpikeAttack (parameter direction)
* BoneRainAttack (parameter direction)
* SniperBoneAttack (parameter interval)
* FoldBonesAttack (parameter direction)
* BoneLazerAttack
* LaserTrapAttack
* GasterBlastersAttack
* AugmentedGasterBlastersAttack
* BlueBoneAttack
* SpearAttack, SpearAttackTwo, SpearAttackThree
* PlatformOneAttack
* HealPlayerAttack (parameter interval hp amount)
* ModeSwitchAttack (to Red Blue Green with direction)
* TeleporterToggleAttack
* CoordinateSystemToggleAttack
* FunctionAttackSpawnerAttack

Each class moves verbatim logic from GamePanel method, adapting to use GameState parameter instead of Test.STATE static (still accessible via static for now but prefer parameter).

**5.4 Create AttackScheduler.java**
* Holds `List<TimelineEntry> timeline` where TimelineEntry = startTick endTick Attack instance + optional interval.
* Build timeline in constructor mirroring current if-else ranges exactly.
* tick() method called each game loop tick iterates active entries and calls attack.tick().
* Replace GamePanel.scheduleAttack() body with `attackScheduler.tick(Test.STATE, Test.STATE.ticks)`

**5.5 Preserve exact tick boundaries**

Translate original conditions precisely:
* Example original `if (Test.ticks >=60 && Test.ticks <120) this.boneSpike(2);`
  becomes TimelineEntry(60,119, new BoneSpikeAttack(2))
* Note inclusive/exclusive edge cases preserved. Audit each of 35 ranges.

Create unit test later to verify timeline mapping matches original counts, but for now manual code review + runtime observation.

**Verification:** compile, run, play through first 2 minutes observe same attack sequence as before: bone spike up at 1s, red mode at 2s, bone shaft at 5s, heal at 10s, cross bones at 15s etc. Use BACKSPACE cheat to skip ahead and spot check later phases: coordinate system at ~267s, spears at ~384s. Compare visually to pre-refactor recording if possible.

**Commit:** maybe split into 3 commits: interface + scheduler skeleton, extract 10 attacks, extract remaining 10 attacks.

---

### Phase 6 — Game Loop and Main split, eliminate statics

**6.1 Create Main.java**
```java
package alpha;
public class Main { public static void main(String[] args){ new Game().start(); } }
```
Keep old `Test.main` delegating to new Main for backward compatibility one commit then remove.

**6.2 Create Game.java**
* Owns GameState instance (non-static).
* Owns GamePanel view, InputHandler, Renderer, AttackScheduler, Systems.
* Implements Runnable game loop formerly in GamePanel.run().
* Method start() creates JFrame, loads sprites, constructs GameState, constructs GamePanel(view), registers input, starts thread.
* Game loop calls systems in same order as old GamePanel.run(): move, physics, collision, lifetime, karma, scheduler tick, etc., then view.repaint().

**6.3 Refactor GamePanel to pure view**
* Remove implements Runnable KeyListener.
* Keep extends JPanel.
* Override paintComponent(Graphics) not paint(), call super.paintComponent, delegate to Renderer.
* Hold reference to GameState passed in constructor for rendering read-only snapshot.
* Remove all game logic methods now delegated to systems.

**6.4 Eliminate Test.java static fields**
* Test class becomes deprecated shim with main forwarding to Main, or deleted outright.
* Update all remaining `Test.STATE.xxx` references to instance `gameState` passed as parameter through constructors.
* This touches nearly every file – do systematic replace.

Alternative incremental: keep Test as holder of static GameState instance during transition then finally delete.

**6.5 Update package imports across all files to remove Test static dependency**

**Verification:** compile with new main class `alpha.Main`. Run `java -cp bin alpha.Main`. Same behavior. Then test old `alpha.Test` still works if shim kept.

**Commit:** 2 commits – introduce Main+Game with Test shim, then delete Test.java and update README.

---

### Phase 7 — Resource loader and cleanup

**7.1 ResourceLoader.java**
* Method `ImageIcon load(String path)` tries `getClass().getResource("/alpha/imgs/...")` then fallback to file system `src/alpha/imgs` for dev.
* Move PNGs to resources folder eventually or keep in src for now but load via classpath.
* Update SpriteLoader to use ResourceLoader.

**7.2 Encoding fix**
* Convert source file encoding from garbled GBK to UTF-8. Translate Chinese comments properly or replace with English. Use iconv or manual review. Ensure `javac -encoding UTF-8` compiles.

**7.3 Vector to ArrayList**
* Replace `Vector<Bone>` etc with `ArrayList` or `CopyOnWriteArrayList`. Game loop single thread mutates, EDT reads during paint – need thread safety. Options:
  * Synchronize on GameState lock during update and during render snapshot copy.
  * Or copy lists to array snapshot at start of render to avoid CME.
* Implement snapshot copy in Renderer: `List<Bone> bonesSnapshot = new ArrayList<>(gs.bones);` then iterate snapshot.

**7.4 Remove dead code**
* `drawPlatform` never called – either wire it up in Renderer or delete Platform class if truly unused. Check platformOne attack adds platform but never moves or renders – decide to either implement rendering or remove attack as dead. Document decision.
* `Test.win` never set – add TODO or leave as is preserving behavior, document as known issue.
* Remove unused imports, unused Bone.png asset or wire it up.

**7.5 Visibility cleanup**
* Make fields private with getters where possible, start with package-private to minimize change then tighten later.
* Add final where appropriate.

**7.6 Update README.md** with new entry point `alpha.Main`, build instructions `javac -encoding UTF-8 -d bin $(find src -name "*.java")` and run `java -cp bin alpha.Main`.

**Verification:** compile with UTF-8 flag, run, visual parity.

**Commit:** resource loader, encoding, Vector replacement separate commits.

---

### Phase 8 — Optional test harness

* Add JUnit 5 dependency via simple jar in lib/ or document manual download since no build tool yet. Or propose migrating to Gradle in follow-up.
* Tests for:
  * CollisionSystem.checkHit with White Blue Orange bone and moving vs stationary soul.
  * KarmaSystem tiered drain timing.
  * FunctionAttack hit detection math.
  * AttackScheduler timeline mapping tick to expected attack class.
  * Direction enum opposite.
* Not required for behavior preservation but recommended.

---

## 4. File Creation Order Summary

Recommended git commit sequence (~25 commits):

1. docs: status and plan
2. model/enums Direction SoulMode BoneColor SpearColor
3. model Entity Bound Shield Warning Platform
4. model Bone Spear Teleporter
5. model Soul
6. model CoordinateSystem + function hierarchy to attack/function
7. GameState container introduction
8. mass replace Test.* to Test.STATE.*
9. system MovementSystem
10. system PhysicsSystem
11. system CollisionSystem
12. system LifetimeSystem
13. system KarmaSystem + ModeSystem
14. input InputHandler InputState
15. render Renderer SpriteLoader FontRegistry
16. attack interface AttackScheduler skeleton
17. attack extract first batch 10 attacks
18. attack extract second batch 10 attacks
19. attack timeline replace scheduleAttack
20. Main Game new entry point with Test shim
21. GamePanel to pure view paintComponent, remove Runnable
22. delete Test.java, update all static references to instance
23. resource ResourceLoader encoding fix
24. Vector to ArrayList snapshot
25. README update cleanup dead code documentation

Each commit message format: `refactor(alpha): <what> - preserve behavior`

---

## 5. Verification Strategy Per Phase

* **Compile check:** `find src -name "*.java" | xargs javac -encoding UTF-8 -d bin -cp src`
* **Manual smoke:** `java -cp bin alpha.Main` (or alpha.Test during transition)
  * Window opens fullscreen centered arena.
  * Press P → pause text toggles.
  * Arrows move soul, red mode clamped to border.
  * Wait to tick ~900 observe crossBones pattern same as before.
  * BACKSPACE skip ahead, F8 heal cheat works.
  * Take damage, observe HP decrease and karma purple bar, invincibility flicker.
  * Blue mode gravity arrow visible, Green mode shield visible and blocks magenta spears.
  * No crash, no freeze, no visual regression obvious.
* **Diff behavior checklist** after Phase 5 and Phase 6 major refactors:
  * Record 60-second gameplay video before refactor as baseline, compare after visually or at least spot-check attack timings at 5s, 15s, 65s, 105s, 267s, 384s.
  * Use BACKSPACE to jump to those ticks quickly.
* **Static analysis:** no more `Test.` static field accesses after Phase 6 except possibly constants. Grep to verify: `grep -rn "Test\.STATE" src` should be 0 after final.
* **File count:** after completion `find src/alpha -name "*.java" | wc -l` should be ~45-50.

---

## 6. Risks and Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| String == to enum migration breaks mode checks | Game logic wrong mode | Use .equals transition first, add unit test for SoulMode.fromString, keep toString backward compat, grep for `==` after |
| Vector to ArrayList causes ConcurrentModification during paint | Crash | Snapshot copy in Renderer before iteration, synchronize update vs render on GameState lock |
| Static to instance refactor misses a reference | NPE or compile error | Compile after each commit catches, grep for Test\. |
| Attack timeline off-by-one tick vs original if-else | Attack pattern shift | Unit test timeline boundaries, manual spot check at phase transitions, preserve inclusive/exclusive exactly as original |
| Resource path change breaks image loading | Invisible soul | ResourceLoader fallback to file path, log warning if not found, manual visual check |
| Swing threading violation surfacing after refactor | Flicker or race | Keep same threading model initially (game thread mutates, EDT reads snapshot), later consider Swing Timer but out of scope Phase1 |
| Encoding conversion corrupts Chinese comments | Compile error or mojibake | Use iconv GBK to UTF-8, verify javac -encoding UTF-8 succeeds, keep English replacement option |
| Dead platform feature removal changes behavior (none currently) | No impact | Document as known dead code, optionally wire up rendering in Phase7 as enhancement not required |

---

## 7. Post-Split Enhancements (Out of Scope for splitting plan but noted)

* Migrate to Gradle or Maven for build and JUnit dependency management.
* Replace Swing Timer or game thread with proper game loop using nanoTime delta.
* Add win condition at end of timeline or loop timeline.
* Implement Platform rendering or remove Platform attack.
* Sprite for Bone.png actually used instead of colored rectangles.
* Audio support.
* Configuration file for attack timeline JSON instead of hardcoded Java.
* Save high score, pause menu.
* Unit test coverage target 70% for system and model packages.

---

## 8. Acceptance Criteria for Splitting Complete

* `src/alpha/Test.java` deleted, replaced by ~45 files across subpackages.
* `alpha.Main` is entry point, `java -cp bin alpha.Main` launches identical gameplay.
* Zero static mutable fields in codebase except maybe constants final.
* GamePanel under 200 lines, only view responsibilities.
* AttackScheduler data-driven, no 167-line if-else.
* README updated with build/run instructions.
* Manual smoke test passes checklist from section 5.
* `git log --oneline` shows incremental commits per phase, each compiling.

---

## 9. Immediate Next Actions

1. Create branch `refactor/split-alpha`.
2. Execute Phase 1 step 1.1 enums creation – first commit.
3. Proceed through Phase 1 model extraction file by file verifying compile each time.
4. Do not proceed to Phase 2 until Phase 1 compiles and runs.

Want me to start Phase 1 now – create enums and extract Entity hierarchy – or do you want to adjust target package structure first?
