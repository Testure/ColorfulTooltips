package colorfultooltips;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.compat.mods.ModPropertyContainer;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;

import java.util.List;
import java.util.function.BiPredicate;

public class Groovy {
    public static final ModSupport.Container<GroovySupport> CONTAINER = new ModSupport.Container<>(ColorfulTooltips.MODID, ColorfulTooltips.NAME, GroovySupport::new);

    public static class GroovySupport extends ModPropertyContainer {
        public final Tooltips tooltips = new Tooltips();

        public GroovySupport() {
            addRegistry(tooltips);
        }
    }

    public static class Tooltips extends VirtualizedRegistry<ITooltipColor> {
        @Override
        @GroovyBlacklist
        public void onReload() {
            ColorfulTooltips.reloadColors();
        }

        public void add(ITooltipColor color, int priority) {
            if (color == null) return;
            if (priority < 0 || priority > (color.isGeneric() ? ColorfulTooltips.GENERIC_COLORS.size() : ColorfulTooltips.STACK_COLORS.size())) return;
            ColorfulTooltips.addColor(color, priority);
        }

        public void add(ITooltipColor color) {
            if (color == null) return;
            ColorfulTooltips.addColor(color);
        }

        public void add(int color, IIngredient ingredient) {
            add((ingredient instanceof OreDictIngredient) ? new OreDictTooltipColor(color, ((OreDictIngredient) ingredient).getOreDict()) : new ItemStackTooltipColor(color, ingredient.getMatchingStacks()));
        }

        public void add(int color, Item item, int meta) {
            add(new ItemTooltipColor(color, item, meta));
        }

        public void add(int color, Item item) {
            add(color, item, -1);
        }

        public void add(IRarity rarity) {
            add(new RarityTooltipColor(rarity));
        }

        public void add(int color, BiPredicate<ItemStack, List<String>> func) {
            add(new ITooltipColor() {
                @Override
                public int getColor() {
                    return color;
                }

                @Override
                public boolean matches(ItemStack stack, List<String> lines) {
                    return func.test(stack, lines);
                }
            });
        }
    }

    public static void init() {}
}
