package me.vemacs.vperms;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class vPermsPlugin extends JavaPlugin {
    @Getter
    private static vPermsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}