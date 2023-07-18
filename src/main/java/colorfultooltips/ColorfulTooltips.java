package colorfultooltips;

import configurator.Configurator;
import configurator.api.BooleanConfigValue;
import configurator.api.Config;
import configurator.api.DoubleConfigValue;
import configurator.api.IntegerConfigValue;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.BotaniaAPI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mod(modid = ColorfulTooltips.MODID, name = ColorfulTooltips.NAME, dependencies = "required:configurator@[1.2.5,)", clientSideOnly = true)
public class ColorfulTooltips {
    public static final String MODID = "colorfultooltips";
    public static final String NAME = "Colorful Tooltips";
    protected static final List<ITooltipColor> STACK_COLORS = new ArrayList<>();
    protected static final List<ITooltipColor> GENERIC_COLORS = new ArrayList<>();
    protected static final BooleanConfigValue CREATE_RARITY_COLORS;
    protected static final IntegerConfigValue BACKGROUND_ALPHA;
    protected static final DoubleConfigValue BACKGROUND_MULTI;

    static {
        Config.Builder builder = Config.Builder.builder().ofType(Config.Type.CLIENT).withName(MODID);

        builder.push("settings");
        CREATE_RARITY_COLORS = builder.define("use_item_rarity", true);
        BACKGROUND_ALPHA = builder.define("tooltip_background_alpha", 230);
        BACKGROUND_MULTI = builder.define("tooltip_background_color_multiplier", 0.1D);
        builder.pop();

        Configurator.registerConfig(builder.build());

        if (Loader.isModLoaded("groovyscript")) Groovy.init();
    }

    public ColorfulTooltips() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.post(new RegisterTooltipColorsEvent());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerColors(RegisterTooltipColorsEvent event) {
        if (Boolean.TRUE.equals(CREATE_RARITY_COLORS.get())) {
            event.addTooltipColor(new RarityTooltipColor(EnumRarity.UNCOMMON));
            event.addTooltipColor(new RarityTooltipColor(EnumRarity.RARE));
            event.addTooltipColor(new RarityTooltipColor(EnumRarity.EPIC));
            if (Loader.isModLoaded("botania")) {
                event.addTooltipColor(new RarityTooltipColor(BotaniaAPI.rarityRelic));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void changeTooltipColors(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getStack();
        List<String> lines = Collections.unmodifiableList(event.getLines());

        for (ITooltipColor color : GENERIC_COLORS) {
            if (color.matches(stack, lines)) {
                setTooltipColor(event, color.getColor(stack));
                return;
            }
        }

        if (!stack.isEmpty()) {
            for (ITooltipColor color : STACK_COLORS) {
                if (color.matches(stack, lines)) {
                    setTooltipColor(event, color.getColor(stack));
                    return;
                }
            }
        }
    }

    protected static void reloadColors() {
        GENERIC_COLORS.clear();
        STACK_COLORS.clear();
        MinecraftForge.EVENT_BUS.post(new RegisterTooltipColorsEvent());
    }

    protected static void addColor(ITooltipColor color, int priority) {
        if (color.isGeneric()) GENERIC_COLORS.add(Math.min(Math.max(priority, 0), GENERIC_COLORS.size()), color);
        else STACK_COLORS.add(Math.min(Math.max(priority, 0), STACK_COLORS.size()), color);
    }

    protected static void addColor(ITooltipColor color) {
        if (color.isGeneric()) GENERIC_COLORS.add(color);
        else STACK_COLORS.add(color);
    }

    private void setTooltipColor(RenderTooltipEvent.Color event, int color) {
        if (color == -1) return;
        int alpha = Objects.requireNonNull(BACKGROUND_ALPHA.get());
        double multi = Objects.requireNonNull(BACKGROUND_MULTI.get());
        if ((color & 0xFF000000) != 0xFF000000) color = color | 0xFF000000;
        Color c = new Color(color);
        event.setBorderStart(color);
        event.setBorderEnd(c.darker().getRGB());
        event.setBackground(new Color(Math.min(Math.max((int)(c.getRed() * multi), 0), 255), Math.min(Math.max((int)(c.getGreen() * multi), 0), 255), Math.min(Math.max((int)(c.getBlue() * multi), 0), 255), Math.min(Math.max(alpha, 0), 255)).getRGB());
    }

    public static class RegisterTooltipColorsEvent extends Event {
        public void addTooltipColor(ITooltipColor color, int priority) {
            ColorfulTooltips.addColor(color, priority);
        }

        public void addTooltipColor(ITooltipColor color) {
            ColorfulTooltips.addColor(color);
        }
    }
}
