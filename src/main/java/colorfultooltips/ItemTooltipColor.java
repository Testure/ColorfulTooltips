package colorfultooltips;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemTooltipColor extends AbstractTooltipColor {
    protected final Item item;
    protected final int meta;

    public ItemTooltipColor(int color, Item item, int meta) {
        super(color);
        this.item = item;
        this.meta = meta;
    }

    public ItemTooltipColor(int color, Item item) {
        this(color, item, -1);
    }

    @Override
    public boolean matches(ItemStack stack, List<String> lines) {
        return stack.getItem() == item && (meta == -1 || stack.getMetadata() == meta);
    }
}
