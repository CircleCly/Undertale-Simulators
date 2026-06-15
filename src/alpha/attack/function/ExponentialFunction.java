package alpha.attack.function;

import java.math.BigDecimal;
import java.math.MathContext;

public class ExponentialFunction extends FunctionAttack {
    public float a;


    public float calculateY(float x) {

        return (float) Math.pow(a, x);

    }

    public ExponentialFunction(float a) {
        super();
        this.a = a;

        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.a);
        this.equation = "f(x)=" + abd.round(new MathContext(3)) + "^x";
    }
}
