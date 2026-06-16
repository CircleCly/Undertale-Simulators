package alpha;

public class AttackScheduler {
    public static void tick(GameState gs, GamePanel gp) {

        if (gs.ticks >= 60 && gs.ticks < 120) {

            gp.boneSpike(2);
        } else if (gs.ticks >= 120 && gs.ticks < 300) {
            gp.redtify();
        } else if (gs.ticks >= 300 && gs.ticks <= 600) {

            gp.boneShaft();
        } else if (gs.ticks > 600 && gs.ticks <= 900) {
            gp.healPlayer(16);
        } else if (gs.ticks > 900 && gs.ticks <= 1500) {
            gp.crossBones();
        } else if (gs.ticks > 1500 && gs.ticks <= 1800) {
            gp.healPlayer(20);

        } else if (gs.ticks > 1800 && gs.ticks <= 2760) {
            gp.boneSpike((int) (Math.random() * 4));
        } else if (gs.ticks > 2760 && gs.ticks <= 3060) {
            gp.healPlayer(20);
            gp.redtify();
        } else if (gs.ticks > 3060 && gs.ticks <= 3690) {
            gp.boneRain(1);
            //gp.bluetify(2);
        } else if (gs.ticks > 3690 && gs.ticks <= 3900) {
            gp.healPlayer(20);
        } else if (gs.ticks > 3900 && gs.ticks <= 4200) {

            int dir = 0;
            if (gs.player.up) {
                dir = 0;
            } else if (gs.player.right) {
                dir = 1;
            } else if (gs.player.down) {
                dir = 2;
            } else if (gs.player.left) {
                dir = 3;
            }
            gp.boneRain(dir);
            //gp.bluetify(2);
        } else if (gs.ticks > 4200 && gs.ticks <= 4500) {
            gp.healPlayer(20);
        } else if (gs.ticks > 4500 && gs.ticks <= 4800) {
            gp.boneRain(3);
            //gp.bluetify(0);
        } else if (gs.ticks > 4800 && gs.ticks <= 5100) {
            gp.healPlayer(20);
        } else if (gs.ticks > 5100 && gs.ticks <= 5400) {
            int dir = 0;
            if (gs.player.up) {
                dir = 2;
            } else if (gs.player.right) {
                dir = 3;
            } else if (gs.player.down) {
                dir = 0;
            } else if (gs.player.left) {
                dir = 1;
            }
            gp.boneRain(dir);
            //gp.bluetify(0);
        } else if (gs.ticks > 5400 && gs.ticks <= 5700) {
            gp.healPlayer(20);
        } else if (gs.ticks > 5700 && gs.ticks <= 6000) {
            gp.boneRain(0);
            //gp.bluetify(1);
        } else if (gs.ticks > 6000 && gs.ticks <= 6300) {
            gp.healPlayer(20);
        } else if (gs.ticks > 6300 && gs.ticks <= 7200) {
            if (gs.ticks % 60 == 0) {
                gp.bluetify((int) (Math.random() * 4));
            }
            gp.sniperBone(90);
        } else if (gs.ticks > 7200 && gs.ticks <= 7800) {
            gp.healPlayer(20);
            gp.redtify();
        } else if (gs.ticks > 7800 && gs.ticks <= 9000) {
            if (Math.random() < 0.5) {
                gp.foldBones(0);
            } else {
                gp.foldBones(1);
            }
        } else if (gs.ticks > 9000 && gs.ticks <= 9600) {
            gp.healPlayer(12);
        } else if (gs.ticks > 9600 && gs.ticks <= 10500) {
            gp.setTeleportersState(true);
            if (gs.ticks % 240 == 0) {
                gp.boneLazer((int) (Math.random() * 4));
            }
        } else if (gs.ticks > 10500 && gs.ticks <= 10800) {
            gp.setTeleportersState(false);
            gp.healPlayer(7);
        } else if (gs.ticks > 10800 && gs.ticks <= 12000) {
            gp.setTeleportersState(true);
            gp.laserTrap();

        } else if (gs.ticks > 12000 && gs.ticks <= 12600) {
            gs.warnings.removeAllElements();
            gp.setTeleportersState(false);
            gp.healPlayer(7);

        } else if (gs.ticks > 12600 && gs.ticks <= 12900) {
            gp.gasterBlasters();
            gp.sniperBone(90);
        } else if (gs.ticks > 12900 && gs.ticks <= 13200) {
            gp.sniperBone((int) (Math.random() * 30 + 10));
        } else if (gs.ticks > 13200 && gs.ticks <= 13500) {
            gp.healPlayer(12);
        } else if (gs.ticks > 13500 && gs.ticks <= 14000) {
            gp.boneRain(2);
            gp.sniperBone(180);

        } else if (gs.ticks > 14000 && gs.ticks <= 14300) {
            gp.healPlayer(3);
        } else if (gs.ticks > 14300 && gs.ticks <= 14900) {
            gp.foldBones(0);
            gp.boneShaft();

        } else if (gs.ticks >= 14900 && gs.ticks <= 15200) {
            gp.healPlayer(7);
        } else if (gs.ticks > 15200 && gs.ticks <= 15680) {
            gp.boneRain((int) (Math.random() * 4));
            //gp.bluetify((int) (Math.random()*  4));
        } else if (gs.ticks > 15680 && gs.ticks <= 16000) {
            gp.healPlayer(8);
            gp.redtify();
        } else if (gs.ticks > 16000 && gs.ticks <= 19600) {
            gp.setCoordinateSystemState(true);
            gs.cSystem.createFunctionAttack();


        } else if (gs.ticks > 19600 && gs.ticks <= 20000) {
            gp.setCoordinateSystemState(false);
            gs.cSystem.functionAttacks.removeAllElements();
            gp.healPlayer(12);
        } else if (gs.ticks > 20000 && gs.ticks <= 20600) {

            gp.augmentedGasterBlasters();
            gp.sniperBone(60);
        } else if (gs.ticks >= 20600 && gs.ticks < 21000) {
            gs.player.hp = gs.player.hpMax;
        } else if (gs.ticks > 21000 && gs.ticks <= 21300) {
            gp.bluetify(2);
            gp.crossBonesHorizontal();
        } else if (gs.ticks > 21300 && gs.ticks <= 22000) {
            gp.healPlayer(13);
        } else if (gs.ticks > 22000 && gs.ticks <= 22420) {
            gp.bluetify(2);
            gp.blueBone();
        } else if (gs.ticks > 22420 && gs.ticks <= 23020) {
            gp.healPlayer(16);
        } else if (gs.ticks > 23020 && gs.ticks <= 23620) {
            gp.spearAttack();
        } else if (gs.ticks > 23620 && gs.ticks <= 23920) {
            gp.healPlayer(12);
        } else if (gs.ticks > 23920 && gs.ticks <= 25000) {
            gp.spearAttackTwo();
        } else if (gs.ticks > 25000 && gs.ticks <= 25400) {
            gp.healPlayer(20);
        } else if (gs.ticks > 25400 && gs.ticks <= 26000) {
            gp.spearAttackThree();
        } else if (gs.ticks >= 26000 && gs.ticks < 26400) {
            gp.healPlayer(20);
            gp.bluetify(2);
        } else if (gs.ticks >= 26400 && gs.ticks < 27000) {
            gp.platformOne();
        }

    
    }
}
