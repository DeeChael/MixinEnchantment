package top.theothers.enchantment.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theothers.enchantment.enchantment.nms.SmeltingTouchEnchantment;

@Mixin(value = Enchantments.class)
public class EnchantmentsMixin {

    private static Enchantment SMELTING_TOUCH;

    @Shadow(prefix = "", aliases = "a(Ljava/lang/String;Lnet/minecraft/world/item/enchantment/Enchantment)Lnet/minecraft/world/item/enchantment/Enchantment")
    private static Enchantment register(String name, Enchantment enchantment) {
        throw new RuntimeException();
    }

    @Inject(method = "<clinit>", at=@At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;iterator()Ljava/util/Iterator;"))
    private static void staticInit(CallbackInfo ci) {
        SMELTING_TOUCH = register("smelting_touch", new SmeltingTouchEnchantment());

    }

}
