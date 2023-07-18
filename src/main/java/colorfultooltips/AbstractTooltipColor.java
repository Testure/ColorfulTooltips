package colorfultooltips;

public abstract class AbstractTooltipColor implements ITooltipColor {
    protected final int color;

    public AbstractTooltipColor(int color) {
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }
}
