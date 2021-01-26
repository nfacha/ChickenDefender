package com.nunofacha.chickendefender.kits;

import com.nunofacha.chickendefender.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private ItemStack[] items;
    private String name;

    public Kit(String key) {
        String configKey = "kits." + key;
        this.name = Main.plugin.getConfig().getString(configKey + ".name");
        Main.logger.info("Loading key " + key + " with name " + name);
        for (String itemKeyId : Main.plugin.getConfig().getConfigurationSection(configKey + ".items").getKeys(false)) {
//            Main.logger.info("Loading item "+itemKeyId);
            String itemKey = configKey + ".items." + itemKeyId;
//            Main.logger.info("Item path "+itemKey);
            //noinspection ConstantConditions
            ItemStack itemStack = new ItemStack(Material.getMaterial(Main.plugin.getConfig().getString(itemKey + ".type")));
//            Main.logger.info("Loaded itemstack of type "+itemStack.getType().toString());
        }
    }
}
