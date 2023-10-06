package me.darksnakex.villagerfollow;

import me.darksnakex.villagerfollow.updater.UpdateChecker;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class VillagerFollowCommand implements CommandExecutor {
    private final VillagerFollow plugin;
    public VillagerFollowCommand(VillagerFollow plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        FileConfiguration messagesConfig = plugin.getMessages();
        if (args.length > 0 && commandSender.hasPermission("villagerfollow.use")) {
            if (args[0].equalsIgnoreCase("version") && commandSender.hasPermission("villagerfollow.version")) {
                commandSender.sendMessage(plugin.nombre + "By DarkSnakeX - Version: " + plugin.version);
                    new UpdateChecker(plugin, 111553).getVersion(version -> {
                        if (!plugin.version.equalsIgnoreCase(version)) {
                            TextComponent message = new TextComponent(plugin.nombre + messagesConfig.getString("version-command.update-available"));
                            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/villagerfollow.111553/"));
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to spigot").create()));
                            commandSender.spigot().sendMessage(message);
                            if(commandSender instanceof Player){
                                ((Player) commandSender).playSound(((Player) commandSender).getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                            }
                        }
                    });
                return true;
            }
            else if(args[0].equalsIgnoreCase("enable") && commandSender.hasPermission("villagerfollow.enable")){
                commandSender.sendMessage(plugin.nombre + messagesConfig.getString("enable-command"));
                plugin.getConfig().set("Config.villager-follow",true);
                plugin.registerConfig();
                return true;
            }
            else if(args[0].equalsIgnoreCase("disable") && commandSender.hasPermission("villagerfollow.disable")){
                commandSender.sendMessage(plugin.nombre + messagesConfig.getString("disable-command"));
                plugin.getConfig().set("Config.villager-follow",false);
                plugin.registerConfig();
                return true;
            }
            else if(args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("villagerfollow.reload")){
                plugin.registerConfig();
                plugin.reloadConfig();
                plugin.reloadMessages();
                plugin.registerprefix();
                commandSender.sendMessage(plugin.nombre + messagesConfig.getString("reload-command"));
                return true;
            } else if (args[0].equalsIgnoreCase("help") && commandSender.hasPermission("villagerfollow.help")) {
                commandSender.sendMessage(plugin.nombre + "\n" +
                        ChatColor.GREEN +"/vf reload: " +
                        ChatColor.GRAY +
                        messagesConfig.getString("help-command.message-reload") +
                        "\n" + ChatColor.GREEN +
                        "/vf enable: " + ChatColor.GRAY +
                        messagesConfig.getString("help-command.message-enable") +
                        "\n" + ChatColor.GREEN +
                        "/vf disable: " + ChatColor.GRAY +
                        messagesConfig.getString("help-command.message-disable") +
                        "\n" + ChatColor.GREEN +
                        "/vf version: " + ChatColor.GRAY +
                        messagesConfig.getString("help-command.message-version"));
                return true;

            }
        }
        return false;
    }



}
