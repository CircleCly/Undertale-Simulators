package alpha.model;

import java.util.Vector;
import alpha.GameState;
import alpha.attack.function.*;

public class CoordinateSystem {
    public boolean activated = false;
    public Vector<FunctionAttack> functionAttacks = new Vector<FunctionAttack>();

    public void createFunctionAttack(GameState gs) {
        LinearFunction lFunction = null;
        QuadraticFunction qFunction = null;
        ExponentialFunction eFunction = null;
        TrigFunction tFunction = null;
        double value = Math.random();
        if (gs.ticks % 300 == 0) {
            if (value < 0.25) {
                lFunction = new LinearFunction((float) ((int) (Math.random() * 20 - 10) * 0.5),
                    (int) (Math.random() * 300 - 150));
                this.functionAttacks.add(lFunction);
            } else if (value >= 0.25 && value < 0.5) {
                qFunction = new QuadraticFunction((float) (((int) (Math.random() * 50 - 25)) * 0.001),
                    (int) (Math.random() * 6 - 3), (int) (Math.random() * 300 - 150));

                this.functionAttacks.add(qFunction);
            } else if (value >= 0.5 && value < 0.65) {
                eFunction = new ExponentialFunction((float) (((int) (Math.random() * 40 + 80)) * 0.01));
                this.functionAttacks.add(eFunction);
            } else {
                String type;
                if (Math.random() <= 0.5) {
                    type = "sin";
                } else {
                    type = "cos";
                }
                tFunction = new TrigFunction(type, (float) ((int) (Math.random() * 300 - 150)), (float) (((int) (Math.random() * 50 - 25)) * 0.002), (float) ((int) (Math.random() * 300 - 150)), (float) ((int) (Math.random() * 300 - 150)));
                this.functionAttacks.add(tFunction);
            }
        }
    }

    public boolean checkIfPlayerHit(GameState gs) {
        float playerX = gs.player.x - 250;
        float playerY = 250 - gs.player.y;
        float y = 0f;
        for (int i = 0; i < this.functionAttacks.size(); i++) {


            for (int j = 0; j <= 24; j++) {

                if (this.functionAttacks.get(i) instanceof LinearFunction) {
                    y = ((LinearFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof QuadraticFunction) {
                    y = ((QuadraticFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof ExponentialFunction) {
                    y = ((ExponentialFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof TrigFunction) {
                    y = ((TrigFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                }
                //System.out.println(this.functionAttacks.size());

                if ((y <= playerY) && y >= (playerY - 25) && this.functionAttacks.get(i).active) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delayDecrease() {
        for (int i = 0; i < this.functionAttacks.size(); i++) {
            if (this.functionAttacks.get(i).delay > 0) {
                this.functionAttacks.get(i).delay--;
            } else if (this.functionAttacks.get(i).delay == 0) {
                this.functionAttacks.get(i).active = true;
            }
        }
    }
}
