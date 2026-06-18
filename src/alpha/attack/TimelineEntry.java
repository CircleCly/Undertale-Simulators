package alpha.attack;

import alpha.GameState;

public class TimelineEntry {
    public final int startTick;
    public final int endTick;
    public final Attack attack;

    public TimelineEntry(int startTick, int endTick, Attack attack) {
        this.startTick = startTick;
        this.endTick = endTick;
        this.attack = attack;
    }

    public boolean isActive(int tick) {
        return tick >= startTick && tick <= endTick;
    }

    public void tick(GameState gs) {
        attack.tick(gs);
    }
}
