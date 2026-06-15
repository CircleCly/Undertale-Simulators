package alpha.model.enums;

public enum BoneColor {
    WHITE("White"),
    BLUE("Blue"),
    ORANGE("Orange");

    private final String label;

    BoneColor(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static BoneColor fromString(String s) {
        if (s == null) return WHITE;
        switch (s) {
            case "White": return WHITE;
            case "Blue": return BLUE;
            case "Orange": return ORANGE;
            default: return WHITE;
        }
    }

    public boolean equalsString(String s) {
        return label.equals(s);
    }
}
