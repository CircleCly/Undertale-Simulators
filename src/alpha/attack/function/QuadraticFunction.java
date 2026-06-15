package alpha.attack.function;

import java.math.BigDecimal;
import java.math.MathContext;

public class QuadraticFunction extends FunctionAttack {
    public float a;
    public float b;
    public float c;

    public float calculateY(float x) {

        return a * x * x + b * x + c;

    }

    public QuadraticFunction(float a, float b, float c) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.a);

        BigDecimal bbd = new BigDecimal(this.b);


        this.equation = "f(x)=" + abd.round(new MathContext(1)) + "x^2+(" + bbd.round(new MathContext(1)) + "x)+(" + (int) this.c + ")";
    }
}
