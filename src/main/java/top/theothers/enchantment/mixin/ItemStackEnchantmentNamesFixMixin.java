package top.theothers.enchantment.mixin;

import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.datafix.fixes.ItemStackEnchantmentNamesFix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackEnchantmentNamesFix.class)
public class ItemStackEnchantmentNamesFixMixin {

    @Final
    @Shadow
    private static Int2ObjectMap<String> MAP;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(Schema outputSchema, boolean changesType, CallbackInfo ci) {
        MAP.put(72, "minecraft:smelting_touch");
    }

}
