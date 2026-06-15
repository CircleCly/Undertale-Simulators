package alpha.attack.function;

import java.math.BigDecimal;
import java.math.MathContext;

public class TrigFunction extends FunctionAttack {
    //y=Asin(omega x + phi);
    //"sin" is sine function
    //"cos" is cosine function
    public String type;
    public float amplitude;
    public float frequency;
    public float phaseShift;
    public float hShift;

    public float calculateY(float x) {
        switch (type) {
            case "sin":
                return (float) (amplitude * Math.sin(frequency * x + phaseShift) + hShift);
            case "cos":
                return (float) (amplitude * Math.cos(frequency * x + phaseShift) + hShift);
            default:
                return 0.0f;
        }
    }

    public TrigFunction(String type, float A, float f, float p, float h) {
        super();
        this.type = type;
        amplitude = A;
        frequency = f;
        phaseShift = p;
        hShift = h;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.amplitude);
        BigDecimal bDecimal = new BigDecimal(this.frequency);
        BigDecimal cDecimal = new BigDecimal(this.phaseShift);
        BigDecimal dDecimal = new BigDecimal(this.hShift);
        switch (this.type) {
            case "sin":
                this.equation = "f(x)=" + abd.round(new MathContext(3)) + "sin(" + bDecimal.round(new MathContext(3)) + "x+(" + cDecimal.round(new MathContext(3)) + "))+(" + dDecimal.round(new MathContext(3)) + ")";
                break;
            case "cos":
                this.equation = "f(x)=" + abd.round(new MathContext(3)) + "cos(" + bDecimal.round(new MathContext(3)) + "x+(" + cDecimal.round(new MathContext(3)) + "))+(" + dDecimal.round(new MathContext(3)) + ")";
                break;
        }

    }

}
