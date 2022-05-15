package top.theothers.enchantment;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import top.theothers.enchantment.mixin.CraftMetaItemMixin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.plugin.java.annotation.plugin.author.Authors;
import org.jetbrains.annotations.NotNull;
import top.theothers.enchantment.mixin.*;

import java.io.File;
import java.util.Arrays;

@Plugin(name = "TheOthers-Enchantment", version = "1.00.0")
@Description("Enchantment extension for TheOthers server")
@Authors({@Author("TheOthersProject"), @Author("DeeChael")})
@ApiVersion(ApiVersion.Target.v1_13)
@Mixin({BowItemMixin.class, BukkitEnchantmentMixin.class, CraftEnchantmentMixin.class, EnchantmentsMixin.class, ItemStackEnchantmentNamesFixMixin.class, NMSItemStackMixin.class, CraftMetaItemMixin.class})
public class Bootstrap4PaperShelled extends PaperShelledPlugin {

    public Bootstrap4PaperShelled(@NotNull PaperShelledPluginLoader loader, @NotNull PaperShelledPluginDescription paperShelledDescription, @NotNull PluginDescriptionFile description, @NotNull File file) {
        super(loader, paperShelledDescription, description, file);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void enchant(EnchantItemEvent event) {
                ItemStack itemStack = event.getItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    LoreUpdatable loreUpdatable = (LoreUpdatable) itemMeta;
                    loreUpdatable.updateLore();
                    itemStack.setItemMeta(itemMeta);
                }
            }

        }, this);
        ((CraftServer) Bukkit.getServer()).getCommandMap().register("others", new Command("others-enchantment") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String aliases, @NotNull String[] args) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.getType() != Material.AIR) {
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setLore(Arrays.asList("Fuck", "You", "Bukkit"));
                        itemStack.setItemMeta(meta);
                    }
                }
                return true;
            }
        });
    }

}
