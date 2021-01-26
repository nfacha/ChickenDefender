package com.nunofacha.chickendefender.kits;

import com.nunofacha.chickendefender.Main;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private String configKey;
    private ItemStack[] items;
    public Kit(String name) {
        this.configKey = "kits."+configKey;
        Main.logger.info("Loading kit "+name);
    }
}
