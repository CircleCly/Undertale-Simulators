package alpha.attack.function;

public class FunctionAttack {
    public int delay = 300;
    public float[] xs = new float[50];
    public float[] ys = new float[50];
    public boolean active = false;
    public String equation = null;

    public FunctionAttack() {

        for (int i = -250, j = 0; i <= 250 && j < 50; i += 10, j++) {
            this.xs[j] = i;
        }
    }

}
