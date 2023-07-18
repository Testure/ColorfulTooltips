package colorfultooltips;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidTooltipColor extends AbstractTooltipColor {
    protected final String fluidName;

    public FluidTooltipColor(int color, Fluid fluid) {
        super(color);
        this.fluidName = fluid.getLocalizedName(new FluidStack(fluid, 1000));
    }

    public FluidTooltipColor(int color, FluidStack stack) {
        super(color);
        this.fluidName = stack.getLocalizedName();
    }

    @Override
    public boolean matches(ItemStack stack, List<String> lines) {
        return lines.get(0).equals(fluidName);
    }
}
