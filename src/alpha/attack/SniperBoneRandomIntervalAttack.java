package alpha.attack;

import alpha.GameState;

public class SniperBoneRandomIntervalAttack implements Attack {
    private final int minInterval;
    private final int maxInterval;
    private SniperBoneAttack currentDelegate;
    private int currentInterval;

    public SniperBoneRandomIntervalAttack(int minInterval, int maxInterval) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        rollInterval();
    }

    private void rollInterval() {
        currentInterval = (int) (Math.random() * (maxInterval - minInterval + 1)) + minInterval;
        currentDelegate = new SniperBoneAttack(currentInterval);
    }

    @Override
    public void tick(GameState gs) {
        // Re-roll interval each tick to match original behavior where interval is passed fresh each scheduler tick
        // Original: gp.sniperBone((int)(Math.random()*30+10))
        // sniperBone internally checks ticks % interval == 0, so changing interval each tick is correct
        rollInterval();
        currentDelegate.tick(gs);
    }
}
