package colorfultooltips;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictTooltipColor extends AbstractTooltipColor {
    protected final String oreDict;

    public OreDictTooltipColor(int color, String oreDict) {
        super(color);
        this.oreDict = oreDict;
    }

    @Override
    public boolean matches(ItemStack stack, List<String> lines) {
        return OreDictionary.getOres(oreDict, false).stream().anyMatch(ore -> ItemStack.areItemStacksEqual(ore, stack));
    }
}
