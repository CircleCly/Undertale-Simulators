package alpha;

import alpha.attack.*;
import alpha.model.enums.SoulMode;
import java.util.ArrayList;
import java.util.List;

public class AttackScheduler {
    private static final List<TimelineEntry> timeline = new ArrayList<>();

    static {
        // Phase 5: data-driven attack timeline
        // Bounds preserve original scheduleAttack if-else behavior (inclusive)
        // Original tick conditions had a few overlapping >= boundaries resolved by if-else priority;
        // effective start ticks below account for that (14901, 20601, 26001).

        timeline.add(new TimelineEntry(60, 119, new BoneSpikeAttack(2)));
        timeline.add(new TimelineEntry(120, 299, new ModeSwitchAttack(SoulMode.RED)));
        timeline.add(new TimelineEntry(300, 600, new BoneShaftAttack()));
        timeline.add(new TimelineEntry(601, 900, new HealPlayerAttack(16)));
        timeline.add(new TimelineEntry(901, 1500, new CrossBonesAttack()));
        timeline.add(new TimelineEntry(1501, 1800, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(1801, 2760, new BoneSpikeRandomAttack()));
        timeline.add(new TimelineEntry(2761, 3060, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(2761, 3060, new ModeSwitchAttack(SoulMode.RED)));
        timeline.add(new TimelineEntry(3061, 3690, new BoneRainAttack(1)));
        timeline.add(new TimelineEntry(3691, 3900, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(3901, 4200, new BoneRainPlayerDirAttack()));
        timeline.add(new TimelineEntry(4201, 4500, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(4501, 4800, new BoneRainAttack(3)));
        timeline.add(new TimelineEntry(4801, 5100, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(5101, 5400, new BoneRainOppositeDirAttack()));
        timeline.add(new TimelineEntry(5401, 5700, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(5701, 6000, new BoneRainAttack(0)));
        timeline.add(new TimelineEntry(6001, 6300, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(6301, 7200, new ModeSwitchRandomBlueAttack()));
        timeline.add(new TimelineEntry(6301, 7200, new SniperBoneAttack(90)));
        timeline.add(new TimelineEntry(7201, 7800, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(7201, 7800, new ModeSwitchAttack(SoulMode.RED)));
        timeline.add(new TimelineEntry(7801, 9000, new FoldBonesRandomAttack()));
        timeline.add(new TimelineEntry(9001, 9600, new HealPlayerAttack(12)));
        timeline.add(new TimelineEntry(9601, 10500, new TeleporterToggleAttack(true)));
        timeline.add(new TimelineEntry(9601, 10500, new BoneLazerRandomAttack()));
        timeline.add(new TimelineEntry(10501, 10800, new TeleporterToggleAttack(false)));
        timeline.add(new TimelineEntry(10501, 10800, new HealPlayerAttack(7)));
        timeline.add(new TimelineEntry(10801, 12000, new TeleporterToggleAttack(true)));
        timeline.add(new TimelineEntry(10801, 12000, new LaserTrapAttack()));
        timeline.add(new TimelineEntry(12001, 12600, new ClearWarningsAttack()));
        timeline.add(new TimelineEntry(12001, 12600, new TeleporterToggleAttack(false)));
        timeline.add(new TimelineEntry(12001, 12600, new HealPlayerAttack(7)));
        timeline.add(new TimelineEntry(12601, 12900, new GasterBlastersAttack()));
        timeline.add(new TimelineEntry(12601, 12900, new SniperBoneAttack(90)));
        timeline.add(new TimelineEntry(12901, 13200, new SniperBoneRandomIntervalAttack(10, 40)));
        timeline.add(new TimelineEntry(13201, 13500, new HealPlayerAttack(12)));
        timeline.add(new TimelineEntry(13501, 14000, new BoneRainAttack(2)));
        timeline.add(new TimelineEntry(13501, 14000, new SniperBoneAttack(180)));
        timeline.add(new TimelineEntry(14001, 14300, new HealPlayerAttack(3)));
        timeline.add(new TimelineEntry(14301, 14900, new FoldBonesAttack(0)));
        timeline.add(new TimelineEntry(14301, 14900, new BoneShaftAttack()));
        timeline.add(new TimelineEntry(14901, 15200, new HealPlayerAttack(7)));
        timeline.add(new TimelineEntry(15201, 15680, new BoneRainRandomAttack()));
        timeline.add(new TimelineEntry(15681, 16000, new HealPlayerAttack(8)));
        timeline.add(new TimelineEntry(15681, 16000, new ModeSwitchAttack(SoulMode.RED)));
        timeline.add(new TimelineEntry(16001, 19600, new CoordinateSystemToggleAttack(true)));
        timeline.add(new TimelineEntry(16001, 19600, new FunctionAttackSpawnerAttack()));
        timeline.add(new TimelineEntry(19601, 20000, new CoordinateSystemToggleAttack(false)));
        timeline.add(new TimelineEntry(19601, 20000, new ClearFunctionAttacksAttack()));
        timeline.add(new TimelineEntry(19601, 20000, new HealPlayerAttack(12)));
        timeline.add(new TimelineEntry(20001, 20600, new AugmentedGasterBlastersAttack()));
        timeline.add(new TimelineEntry(20001, 20600, new SniperBoneAttack(60)));
        timeline.add(new TimelineEntry(20601, 20999, new FullHealAttack()));
        timeline.add(new TimelineEntry(21001, 21300, new ModeSwitchAttack(SoulMode.BLUE, 2)));
        timeline.add(new TimelineEntry(21001, 21300, new CrossBonesHorizontalAttack()));
        timeline.add(new TimelineEntry(21301, 22000, new HealPlayerAttack(13)));
        timeline.add(new TimelineEntry(22001, 22420, new ModeSwitchAttack(SoulMode.BLUE, 2)));
        timeline.add(new TimelineEntry(22001, 22420, new BlueBoneAttack()));
        timeline.add(new TimelineEntry(22421, 23020, new HealPlayerAttack(16)));
        timeline.add(new TimelineEntry(23021, 23620, new SpearAttack()));
        timeline.add(new TimelineEntry(23621, 23920, new HealPlayerAttack(12)));
        timeline.add(new TimelineEntry(23921, 25000, new SpearAttackTwo()));
        timeline.add(new TimelineEntry(25001, 25400, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(25401, 26000, new SpearAttackThree()));
        timeline.add(new TimelineEntry(26001, 26399, new HealPlayerAttack(20)));
        timeline.add(new TimelineEntry(26001, 26399, new ModeSwitchAttack(SoulMode.BLUE, 2)));
        timeline.add(new TimelineEntry(26400, 26999, new PlatformOneAttack()));
    }

    public static void tick(GameState gs) {
        int tick = (int) gs.ticks;
        for (TimelineEntry entry : timeline) {
            if (entry.isActive(tick)) {
                entry.tick(gs);
            }
        }
    }
}
