package top.theothers.enchantment.mixin;

import com.google.common.collect.ImmutableList;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theothers.enchantment.LoreUpdatable;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Implements(@Interface(iface = LoreUpdatable.class, prefix = "theothers$"))
@Mixin(targets = "org.bukkit.craftbukkit.inventory.CraftMetaItem", remap = false)
public abstract class CraftMetaItemMixin {

    private final List<Component> realLore = new ArrayList<>();

    @Shadow
    private List<String> lore;
    
    @Shadow
    public Map<Enchantment, Integer> getEnchants() {
        throw new RuntimeException();
    }

    @Shadow public abstract void addItemFlags(ItemFlag... hideFlags);

    @Shadow public abstract void lore(List<Component> lore);

    @Inject(method = "<init>*", at=@At("TAIL"))
    public void init(CallbackInfoReturnable<Boolean> cir) {
        this.realLore.clear();
        if (this.lore != null) {
            this.realLore.addAll(this.lore.stream().map(Component::text).collect(Collectors.toList()));
        }
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<>();
        for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
            Component component = Component.text(parseEnchantmentName(entry.getKey().getKey().value()) + " " + intToRoman(entry.getValue()));
            component = component.font(null).decoration(TextDecoration.ITALIC, false).color(TextColor.fromHexString("#AAAAAA"));
            lore.add(component);
        }
        lore.addAll(this.realLore);
        lore(lore);
    }

    @Inject(method = "addEnchant", at=@At("RETURN"))
    public void addEnchant(Enchantment ench, int level, boolean ignoreRestrictions, CallbackInfoReturnable<Boolean> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (cir.getReturnValue() != null) {
            if (cir.getReturnValue()) {
                List<Component> lore = new ArrayList<>();
                for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
                    Component component = Component.text(parseEnchantmentName(entry.getKey().getKey().value()) + " " + intToRoman(entry.getValue()));
                    component = component.font(null).decoration(TextDecoration.ITALIC, false).color(TextColor.fromHexString("#AAAAAA"));
                    lore.add(component);
                }
                lore.addAll(this.realLore);
                lore(lore);
            }
        }
        updateLore();
    }

    @Inject(method = "removeEnchant", at=@At("RETURN"))
    public void removeEnchant(Enchantment ench, CallbackInfoReturnable<Boolean> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (cir.getReturnValue() != null) {
            if (cir.getReturnValue()) {
                List<Component> lore = new ArrayList<>();
                for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
                    Component component = Component.text(parseEnchantmentName(entry.getKey().getKey().value()) + " " + intToRoman(entry.getValue()));
                    component = component.font(null).decoration(TextDecoration.ITALIC, false).color(TextColor.fromHexString("#AAAAAA"));
                    lore.add(component);
                }
                lore.addAll(this.realLore);
                lore(lore);
            }
        }
        updateLore();
    }

    @Inject(method = "hasLore", at=@At("RETURN"), cancellable = true)
    public void hasLore(CallbackInfoReturnable<Boolean> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (!(this.lore.size() > this.getEnchants().size())) {
            cir.setReturnValue(false);
        }
        updateLore();
    }

    @Inject(method = "lore()Ljava/util/List;", at=@At("RETURN"), cancellable = true)
    public void lore(CallbackInfoReturnable<List<Component>> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<Component> lore = new ArrayList<>();
        lore.addAll(this.realLore);
        updateLore();
        cir.setReturnValue(lore);
    }

    @Inject(method = "getLore", at=@At("RETURN"), cancellable = true)
    public void getLore(CallbackInfoReturnable<List<String>> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        updateLore();
        cir.setReturnValue(PaperAdventure.asJson(this.realLore));
    }

    @Inject(method = "getLoreComponents", at=@At("RETURN"), cancellable = true)
    public void getLoreComponents(CallbackInfoReturnable<List<BaseComponent[]>> cir) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<BaseComponent[]> lore = new ArrayList<>();
        for (Component line : this.realLore) {
            lore.add(new BaseComponent[] {new TextComponent(PaperAdventure.asPlain(line, Locale.ENGLISH))});
        }
        updateLore();
        cir.setReturnValue(lore);
    }

    @Inject(method = "setLore", at = @At("HEAD"), cancellable = true)
    public void setLore(List<String> lore, CallbackInfo ci) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<Component> list = new ArrayList<>();
        for (String line : lore) {
            list.add(Component.text(line));
        }
        lore(list);
        ci.cancel();
    }

    @Inject(method = "setLoreComponents", at = @At("HEAD"))
    public void setLoreComponents(List<BaseComponent[]> lore, CallbackInfo ci) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.realLore.clear();
        this.realLore.addAll(PaperAdventure.asAdventureFromJson(lore.stream().map(ComponentSerializer::toString).collect(Collectors.toList())));
        List<Component> list = new ArrayList<>();
        for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
            Component component = Component.text(parseEnchantmentName(entry.getKey().getKey().value()) + " " + intToRoman(entry.getValue()));
            component = component.font(null).decoration(TextDecoration.ITALIC, false).color(TextColor.color(170, 170, 170));
            list.add(component);
        }
        list.addAll(this.realLore);
        lore(list);
        ci.cancel();
    }

    @ModifyVariable(method = "lore(Ljava/util/List;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public List<Component> mixin$lore(List<Component> lore) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.realLore.clear();
        this.realLore.addAll(lore);
        lore = new ArrayList<>();
        for (Entry<Enchantment, Integer> entry : getEnchants().entrySet()) {
            Component component = Component.text(parseEnchantmentName(entry.getKey().getKey().value()) + " " + intToRoman(entry.getValue()));
            component = component.font(null).decoration(TextDecoration.ITALIC, false).color(TextColor.fromHexString("#AAAAAA"));
            lore.add(component);
        }
        lore.addAll(this.realLore);
        return lore;
    }

    @ModifyArg(method = "clone()Lorg/bukkit/craftbukkit/inventory/CraftMetaItem;", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;<init>(Ljava/util/Collection;)V"))
    public Collection<? extends String> clone(Collection<? extends String> elements) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return new ArrayList<>(PaperAdventure.asJson(this.realLore));
    }

    @ModifyArg(method = "serialize(Lcom/google/common/collect/ImmutableMap$Builder;)Lcom/google/common/collect/ImmutableMap$Builder;", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    public Collection<? extends String> serialize0(Collection<? extends String> elements) {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return ImmutableList.copyOf(PaperAdventure.asJson(this.realLore));
    }

    private String parseEnchantmentName(String id) {
        if (id.contains("_")) {
            String[] split = id.split("_");
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = Arrays.stream(split).iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                builder.append(String.valueOf(next.charAt(0)).toUpperCase()).append(next.substring(1));
                if (iterator.hasNext()) {
                    builder.append(" ");
                }
            }
            return builder.toString();
        } else {
            return String.valueOf(id.charAt(0)).toUpperCase() + id.substring(1);
        }
    }

    private String intToRoman(int number) {
        String romanNumber = "";
        int[] numbers =        {1000, 900,  500, 400,  100,  90,  50,   40,  10,   9,    5,   4,    1};
        String[] romanNumbers = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < numbers.length; i++) {
            while (numbers[i] <= number) {
                number -= numbers[i];
                romanNumber = romanNumber.concat(romanNumbers[i]);
            }
            if (number == 0) break;
        }
        return romanNumber;
    }

    void updateLore() {
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore(this.realLore);
    }

}
