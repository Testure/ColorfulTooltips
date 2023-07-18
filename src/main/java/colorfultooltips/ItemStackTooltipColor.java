package colorfultooltips;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class ItemStackTooltipColor extends AbstractTooltipColor {
    protected NonNullList<ItemStack> validStacks;

    public ItemStackTooltipColor(int color, ItemStack... stacks) {
        super(color);
        validStacks = NonNullList.from(ItemStack.EMPTY, stacks);
    }

    @Override
    public boolean matches(ItemStack stack, List<String> lines) {
        return validStacks.stream().anyMatch(s -> ItemStack.areItemStacksEqual(s, stack));
    }
}
