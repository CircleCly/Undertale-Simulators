package alpha.model.enums;

public enum SoulMode {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green");

    private final String label;

    SoulMode(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static SoulMode fromString(String s) {
        if (s == null) return RED;
        switch (s) {
            case "Red": return RED;
            case "Blue": return BLUE;
            case "Green": return GREEN;
            default: return RED;
        }
    }

    public boolean equalsString(String s) {
        return label.equals(s);
    }
}
