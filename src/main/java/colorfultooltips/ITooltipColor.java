package colorfultooltips;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ITooltipColor {
    int getColor();

    default int getColor(ItemStack stack) {
        return getColor();
    }

    boolean matches(ItemStack stack, List<String> lines);

    default boolean isGeneric() {
        return false;
    }
}
