package top.theothers.enchantment.mixin;

import net.minecraft.core.Registry;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CraftEnchantment.class, remap = false)
public class CraftEnchantmentMixin {

    @Final
    @Shadow(remap = false)
    private net.minecraft.world.item.enchantment.Enchantment target;

    /*
    @Inject(method = "getName", at=@At("HEAD"), remap = false, cancellable = true)
    public void getName(CallbackInfoReturnable<String> cir) {
        switch(Registry.ENCHANTMENT.getId(this.target)) {
            case 0:
                cir.setReturnValue("SMELTING_TOUCH");
                break;
            case 1:
                cir.setReturnValue("PROTECTION_ENVIRONMENTAL");
                break;
            case 2:
                cir.setReturnValue("PROTECTION_FIRE");
                break;
            case 3:
                cir.setReturnValue("PROTECTION_FALL");
                break;
            case 4:
                cir.setReturnValue("PROTECTION_EXPLOSIONS");
                break;
            case 5:
                cir.setReturnValue("PROTECTION_PROJECTILE");
                break;
            case 6:
                cir.setReturnValue("OXYGEN");
                break;
            case 7:
                cir.setReturnValue("WATER_WORKER");
                break;
            case 8:
                cir.setReturnValue("THORNS");
                break;
            case 9:
                cir.setReturnValue("DEPTH_STRIDER");
                break;
            case 10:
                cir.setReturnValue("FROST_WALKER");
                break;
            case 11:
                cir.setReturnValue("BINDING_CURSE");
                break;
            case 12:
                cir.setReturnValue("SOUL_SPEED");
                break;
            case 13:
                cir.setReturnValue("DAMAGE_ALL");
                break;
            case 14:
                cir.setReturnValue("DAMAGE_UNDEAD");
                break;
            case 15:
                cir.setReturnValue("DAMAGE_ARTHROPODS");
                break;
            case 16:
                cir.setReturnValue("KNOCKBACK");
                break;
            case 17:
                cir.setReturnValue("FIRE_ASPECT");
                break;
            case 18:
                cir.setReturnValue("LOOT_BONUS_MOBS");
                break;
            case 19:
                cir.setReturnValue("SWEEPING_EDGE");
                break;
            case 20:
                cir.setReturnValue("DIG_SPEED");
                break;
            case 21:
                cir.setReturnValue("SILK_TOUCH");
                break;
            case 22:
                cir.setReturnValue("DURABILITY");
                break;
            case 23:
                cir.setReturnValue("LOOT_BONUS_BLOCKS");
                break;
            case 24:
                cir.setReturnValue("ARROW_DAMAGE");
                break;
            case 25:
                cir.setReturnValue("ARROW_KNOCKBACK");
                break;
            case 26:
                cir.setReturnValue("ARROW_FIRE");
                break;
            case 27:
                cir.setReturnValue("ARROW_INFINITE");
                break;
            case 28:
                cir.setReturnValue("LUCK");
                break;
            case 29:
                cir.setReturnValue("LURE");
                break;
            case 30:
                cir.setReturnValue("LOYALTY");
                break;
            case 31:
                cir.setReturnValue("IMPALING");
                break;
            case 32:
                cir.setReturnValue("RIPTIDE");
                break;
            case 33:
                cir.setReturnValue("CHANNELING");
                break;
            case 34:
                cir.setReturnValue("MULTISHOT");
                break;
            case 35:
                cir.setReturnValue("QUICK_CHARGE");
                break;
            case 36:
                cir.setReturnValue("PIERCING");
                break;
            case 37:
                cir.setReturnValue("MENDING");
                break;
            case 38:
                cir.setReturnValue("VANISHING_CURSE");
                break;
            default:
                cir.setReturnValue("UNKNOWN_ENCHANT_" + Registry.ENCHANTMENT.getId(this.target));
                break;
        }
    }
    */

}
