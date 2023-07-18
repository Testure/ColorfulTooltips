package colorfultooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;

import java.util.List;

public class RarityTooltipColor extends AbstractTooltipColor {
    protected final String rarity;

    public RarityTooltipColor(int color, IRarity rarity) {
        super(color);
        this.rarity = rarity.getName();
    }

    public RarityTooltipColor(IRarity rarity) {
        this(Minecraft.getMinecraft().fontRenderer.getColorCode(rarity.getColor().toString().charAt(1)), rarity);
    }

    @Override
    public boolean matches(ItemStack stack, List<String> lines) {
        return stack.getItem().getForgeRarity(stack).getName().equals(rarity);
    }
}
