package me.darksnakex.villagerfollow.updater;

import me.darksnakex.villagerfollow.VillagerFollow;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;


public class NotifyAdmin implements Listener {

    private static VillagerFollow plugin;

    public NotifyAdmin(VillagerFollow plugin) {
        NotifyAdmin.plugin = plugin;
    }

    @EventHandler
    public void notifyUpdate(PlayerJoinEvent event){
        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessages();
        Player player = event.getPlayer();
        if(Objects.equals(config.getString("Config.check-update"), "true") && player.isOp()) {
            new UpdateChecker(plugin, 111553).getVersion(version -> {
                if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                    TextComponent message = new TextComponent(plugin.nombre + messagesConfig.getString("version-command.update-available"));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/villagerfollow.111553/"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to spigot").create()));
                    player.spigot().sendMessage(message);
                }
            });
        }
    }


}
