package alpha.model.enums;

public enum SpearColor {
    MAGENTA("Magenta"),
    YELLOW("Yellow");

    private final String label;

    SpearColor(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static SpearColor fromString(String s) {
        if (s == null) return MAGENTA;
        switch (s) {
            case "Magenta": return MAGENTA;
            case "Yellow": return YELLOW;
            default: return MAGENTA;
        }
    }

    public boolean equalsString(String s) {
        return label.equals(s);
    }
}
