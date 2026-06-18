package alpha.attack.function;

import java.math.BigDecimal;
import java.math.MathContext;

public class LinearFunction extends FunctionAttack {
    public float k;
    public float b;

    public float calculateY(float x) {

        return x * k + b;

    }

    public LinearFunction(float k, float b) {
        super();
        this.k = k;
        this.b = b;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal kBigDecimal = new BigDecimal(this.k);


        this.equation = "f(x)=" + kBigDecimal.round(new MathContext(2)) + "x+(" + (int) this.b + ")";
    }
}
