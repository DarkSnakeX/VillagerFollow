package me.darksnakex.villagerfollow.updater;

import me.darksnakex.villagerfollow.VillagerFollow;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final VillagerFollow plugin;
    private final int resourceId;

    public UpdateChecker(VillagerFollow plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        FileConfiguration config = plugin.getConfig();
        if (Objects.equals(config.getString("Config.check-update"), "true")) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scann = new Scanner(is)) {
                    if (scann.hasNext()) {
                        consumer.accept(scann.next());
                    }
                } catch (IOException e) {
                    plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
                }
            });
        }
    }
}
