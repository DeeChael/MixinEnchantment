package top.theothers.enchantment.mixin;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class, remap = false)
public class BukkitEnchantmentMixin {

    private static final Enchantment SMELTING_TOUCH = new EnchantmentWrapper("smelting_touch");

    @Inject(method = "getByKey", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getByKey(NamespacedKey key, CallbackInfoReturnable<Enchantment> cir) {
        if (key.value().contains("smelting")) {
            cir.setReturnValue(SMELTING_TOUCH);
        }
    }

    @Inject(method = "getByName", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getByName(String name, CallbackInfoReturnable<Enchantment> cir) {
        if (name.contains("smelting")) {
            cir.setReturnValue(SMELTING_TOUCH);
        }
    }

}
