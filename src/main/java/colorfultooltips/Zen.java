package colorfultooltips;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.ColorfulTooltips")
@ZenRegister
public class Zen {
    @ZenMethod
    public static void add(int color, IIngredient ingredient) {
        if (ingredient instanceof IOreDictEntry) ColorfulTooltips.addColor(new OreDictTooltipColor(color, ((IOreDictEntry) ingredient).getName()));
        else ColorfulTooltips.addColor(new ItemStackTooltipColor(color, CraftTweakerMC.getIngredient(ingredient).getMatchingStacks()));
    }

    @ZenMethod
    public static void add(int color, String oreDict) {
        ColorfulTooltips.addColor(new OreDictTooltipColor(color, oreDict));
    }

    @ZenMethod
    public static void add(int color, IItemDefinition item, int meta) {
        Item real = CraftTweakerMC.getItem(item);
        ColorfulTooltips.addColor(new ItemTooltipColor(color, real, meta));
    }

    @ZenMethod
    public static void add(int color, IItemDefinition item) {
        add(color, item, -1);
    }
}
