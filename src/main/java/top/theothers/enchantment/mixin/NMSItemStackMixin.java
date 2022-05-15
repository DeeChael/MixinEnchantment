package top.theothers.enchantment.mixin;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theothers.enchantment.LoreUpdatable;

import java.util.*;

@Mixin(ItemStack.class)
public abstract class NMSItemStackMixin {

    @Shadow
    private int count;
    @Shadow
    private Item item;
    @Shadow
    public CompoundTag tag;

    @Shadow public abstract ItemStack split(int amount);

    @Shadow public abstract org.bukkit.inventory.ItemStack asBukkitMirror();

    private final List<StringTag> realLore = new ArrayList<>();

    /*
    @Inject(method = "processEnchantOrder", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getList(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;"))
    public void processEnchantOrder(CompoundTag tag, CallbackInfo ci) {
        if (tag != null) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("Lore", 9)) {
                ListTag lore = new ListTag();
                lore.addAll(realLore);
                display.put("Lore", lore);
            }
        }
    }

    @ModifyArg(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;"), index = 1)
    public Tag save(Tag element) {
        CompoundTag nbt = (CompoundTag) element;
        CompoundTag display = nbt.getCompound("display");
        if (display.contains("Lore", 9)) {
            ListTag lore = new ListTag();
            lore.addAll(realLore);
            display.put("Lore", lore);
        }
        return element;
    }

    @ModifyVariable(method = "setTag", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    public CompoundTag setTag(CompoundTag nbt) {
        if (nbt != null) {
            CompoundTag display = nbt.getCompound("display");
            if (display.contains("Lore", 9)) {
                ListTag finalLore = new ListTag();
                if (nbt.contains("Enchantments", 9)) {
                    List<StringTag> enchantmentLore = new ArrayList<>();
                    ListTag enchantments = nbt.getList("Enchantments", 10);
                    for(int i = 0; i < enchantments.size(); ++i) {
                        CompoundTag compoundTag = enchantments.getCompound(i);
                        String enchantmentName = parseEnchantmentName(compoundTag.getString("id"));
                        int level = Mth.clamp(compoundTag.getInt("lvl"), 0, 255);
                        enchantmentLore.add(StringTag.valueOf(CraftChatMessage.toJSON(new TextComponent(enchantmentName + " " + level))));
                    }
                    finalLore.addAll(enchantmentLore);
                }
                finalLore.addAll(this.realLore);
                display.put("Lore", finalLore);
            }
        }
        return nbt;
    }

    @Inject(method = "getTag", at = @At(value = "RETURN"), cancellable = true)
    public void getTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if (tag != null) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("Lore", 9)) {
                ListTag lore = new ListTag();
                lore.addAll(realLore);
                display.put("Lore", lore);
            }
            cir.setReturnValue(tag);
        }
    }

    @Inject(method = "getTagClone", at = @At(value = "RETURN"), cancellable = true)
    public void getTagClone(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if (tag != null) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("Lore", 9)) {
                ListTag lore = new ListTag();
                lore.addAll(realLore);
                display.put("Lore", lore);
            }
            cir.setReturnValue(tag);
        }
    }

    @Inject(method = "getOrCreateTag", at = @At(value = "RETURN"), cancellable = true)
    public void getOrCreateTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        CompoundTag display = tag.getCompound("display");
        if (display.contains("Lore", 9)) {
            ListTag lore = new ListTag();
            lore.addAll(realLore);
            display.put("Lore", lore);
        }
        cir.setReturnValue(tag);
    }

    @Inject(method = "getOrCreateTagElement", at = @At(value = "RETURN"), cancellable = true)
    public void getOrCreateTagElement(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        CompoundTag display = tag.getCompound("display");
        if (display.contains("Lore", 9)) {
            ListTag lore = new ListTag();
            lore.addAll(realLore);
            display.put("Lore", lore);
        }
        cir.setReturnValue(tag);
    }

    @Inject(method = "getTagElement", at = @At(value = "RETURN"), cancellable = true)
    public void getTagElement(String key, CallbackInfoReturnable<CompoundTag> cir) {
        if (Objects.equals(key, "display")) {
            CompoundTag display = cir.getReturnValue();
            if (display != null) {
                if (display.contains("Lore", 9)) {
                    ListTag lore = new ListTag();
                    lore.addAll(realLore);
                    display.put("Lore", lore);
                }
                cir.setReturnValue(display);
            }
        }
    }

    @Inject(method = "getOrCreateTagElement", at = @At(value = "RETURN"), cancellable = true)
    public void getOrCreateTagElement(String key, CallbackInfoReturnable<CompoundTag> cir) {
        if (Objects.equals(key, "display")) {
            CompoundTag display = cir.getReturnValue();
            if (display != null) {
                if (display.contains("Lore", 9)) {
                    ListTag lore = new ListTag();
                    lore.addAll(realLore);
                    display.put("Lore", lore);
                }
                cir.setReturnValue(display);
            }
        }
    }

    @Inject(method = "addTagElement", at = @At(value = "HEAD"))
    public void addTagElement(String key, Tag element, CallbackInfo ci) {
        if (Objects.equals(key, "display")) {
            CompoundTag display = (CompoundTag) element;
            if (display.contains("Lore", 9)) {
                ListTag lore = new ListTag();
                lore.addAll(realLore);
                display.put("Lore", lore);
            }
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundTag;getCompound(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;"))
    public void load(CompoundTag nbt, CallbackInfo ci) {
        CompoundTag display = this.tag.getCompound("display");
        if (nbt.contains("RealLore", 9)) {
            ListTag realLore = nbt.getList("realLore", 8);
            this.realLore.clear();
            for (Tag line : realLore) {
                this.realLore.add((StringTag) line);
            }
        } else {
            if (display.contains("Lore", 9)) {
                ListTag lore = display.getList("Lore", 8);
                realLore.clear();
                for(int index = 0; index < lore.size(); ++index) {
                    String json = lore.getString(index);
                    if (json.contains("§")) {
                        try {
                            realLore.add(this.copiedMethod$convert(json));
                        } catch (JsonParseException var6) {
                            realLore.add(StringTag.valueOf(CraftChatMessage.toJSON(new TextComponent(""))));
                        }
                    }
                }
            }
        }
        ListTag finalLore = new ListTag();
        if (tag.contains("Enchantments", 9)) {
            List<StringTag> enchantmentLore = new ArrayList<>();
            ListTag enchantments = tag.getList("Enchantments", 10);
            for(int i = 0; i < enchantments.size(); ++i) {
                CompoundTag compoundTag = enchantments.getCompound(i);
                String enchantmentName = parseEnchantmentName(compoundTag.getString("id"));
                int level = Mth.clamp(compoundTag.getInt("lvl"), 0, 255);
                enchantmentLore.add(StringTag.valueOf(CraftChatMessage.toJSON(new TextComponent(enchantmentName + " " + level))));
            }
            finalLore.addAll(enchantmentLore);
        }
        finalLore.addAll(this.realLore);
        ListTag rL = new ListTag();
        nbt.put("RealLore", rL);
        nbt.putInt("HideFlags", nbt.getInt("HideFlags") | ItemStack.TooltipPart.ENCHANTMENTS.getMask());
        display.put("Lore", finalLore);
    }

    private StringTag copiedMethod$convert(String json) {
        Component component = Component.Serializer.fromJson(json);
        if (component instanceof TextComponent && (component).getContents().contains("§") && ((Component)component).getSiblings().isEmpty()) {
            component = CraftChatMessage.fromString((component).getContents())[0];
        }

        return StringTag.valueOf(CraftChatMessage.toJSON(component));
    }

    private String parseEnchantmentName(String id) {
        if (id.contains("_")) {
            String[] split = id.split("_");
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = Arrays.stream(split).iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                builder.append(next.charAt(0)).append(next.substring(1));
                if (iterator.hasNext()) {
                    builder.append(" ");
                }
            }
            return builder.toString();
        } else {
            return String.valueOf(id.charAt(0)).toUpperCase() + id.substring(1);
        }
    }
     */

}
