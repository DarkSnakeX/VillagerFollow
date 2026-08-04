package me.darksnakex.villagerfollow;


import me.darksnakex.villagerfollow.interactions.VillagerInteractionDropEmerald;
import me.darksnakex.villagerfollow.interactions.VillagerInteractionGiveEmerald;
import me.darksnakex.villagerfollow.interactions.VillagerInteractionHandEmerald;
import me.darksnakex.villagerfollow.updater.NotifyAdmin;
import me.darksnakex.villagerfollow.updater.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.Objects;


public class VillagerFollow extends JavaPlugin {

    public String rutaConfig;
    private YamlConfiguration messages = null;
    private File messagesFile = null;
    public final PluginDescriptionFile pdffile = getDescription();
    public final String version = pdffile.getVersion();
    public String nombre = ChatColor.YELLOW+"["+ ChatColor.GREEN+pdffile.getName()+ChatColor.YELLOW+"] "+ChatColor.GRAY;
    public static boolean isSpigot = true;

    @Override
    public void onEnable() {
        registerPrefix();
        Bukkit.getConsoleSender().sendMessage(nombre + "has been enabled");
        registerMessages();
        registerCommands();
        registerEvents();
        registerConfig();
        checkMissingConfigKeys();

        if (isRunningOnPaper()) {
            Bukkit.getConsoleSender().sendMessage(nombre + "Using Paper version...");
            isSpigot = false;
        } else {
            Bukkit.getConsoleSender().sendMessage(nombre + "Using Spigot version...");
        }

        try {
            int pluginId = 19294;
            new Metrics(this,pluginId);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(nombre + "Error while sending metrics data: " + e.getMessage());
        }

        new UpdateChecker(this, 111553).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(nombre + "There is not a new update available.");
            } else {
                Bukkit.getConsoleSender().sendMessage(nombre + "There is a new update available.");
            }
        });
    }


    public static boolean isRunningOnPaper() {
        try {
            Class.forName("io.papermc.paper.configuration.Configuration");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(nombre + "has been disabled");
    }

    public void registerCommands(){
        Objects.requireNonNull(getCommand("villagerfollow")).setExecutor(new VillagerFollowCommand(this));
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new VillagerInteractionHandEmerald(this), this);
        pm.registerEvents(new NotifyAdmin(this),this);
        pm.registerEvents(new VillagerInteractionDropEmerald(this),this);
        pm.registerEvents(new VillagerInteractionGiveEmerald(this),this);
    }

    public void registerConfig(){
        VillagerInteractionHandEmerald.onPluginReload();
        VillagerInteractionDropEmerald.onPluginReload2();
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    public FileConfiguration getMessages(){
        if(messages == null){
            reloadMessages();
        }
        return messages;
    }

    @SuppressWarnings("all")
    public void reloadMessages(){
        if(messages == null){
            messagesFile = new File(getDataFolder(),"messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("messages.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                messages.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    public void saveMessages(){
        try{
            messages.save(messagesFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void registerMessages(){
        messagesFile = new File(getDataFolder(),"messages.yml");
        if(!messagesFile.exists()){
            saveResource("messages.yml", false);
        }
    }

    public void registerPrefix(){
        FileConfiguration messagesConfig = getMessages();
        nombre = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("prefix") +  " §7");
    }

    private void checkMissingConfigKeys() {
        FileConfiguration config = getConfig();

        boolean modified = false;

        if (!config.isSet("Config.check-update")) {
            config.set("Config.check-update", true);
            modified = true;
        }

        if (!config.isSet("Config.villager-follow")) {
            config.set("Config.villager-follow", true);
            modified = true;

        }

        if (!config.isSet("Config.villager-follow-player")) {
            config.set("Config.villager-follow-player", true);
            modified = true;

        }

        if (!config.isSet("Config.villager-follow-radius")) {
            config.set("Config.villager-follow-radius", 12);
            modified = true;

        }

        if (!config.isSet("Config.villager-allow-goto-emerald")) {
            config.set("Config.villager-allow-goto-emerald", true);
            modified = true;

        }

        if (!config.isSet("Config.villager-allow-catch-emerald")) {
            config.set("Config.villager-allow-catch-emerald", true);
            modified = true;

        }

        if (!config.isSet("Config.villager-pay-follow")) {
            config.set("Config.villager-pay-follow", true);
            modified = true;

        }

        if (!config.isSet("Config.villager-pay-follow-time")) {
            config.set("Config.villager-pay-follow-time", 120);
            modified = true;

        }

        if (!config.isSet("Config.villager-allow-catch-emerald-heal")) {
            config.set("Config.villager-allow-catch-emerald-heal", true);
            modified = true;

        }

        if (modified) {
            saveConfig();
        }
    }

}