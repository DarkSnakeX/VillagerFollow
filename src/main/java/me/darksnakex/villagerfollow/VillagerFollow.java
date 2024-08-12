package me.darksnakex.villagerfollow;


import me.darksnakex.villagerfollow.interactions.VillagerInteractionDropEmerald;
import me.darksnakex.villagerfollow.interactions.VillagerInteractionGiveEmerald;
import me.darksnakex.villagerfollow.interactions.VillagerInteractionHandEmerald;
import me.darksnakex.villagerfollow.bstats.Metrics;
import me.darksnakex.villagerfollow.updater.NotifyAdmin;
import me.darksnakex.villagerfollow.updater.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;


public final class VillagerFollow extends JavaPlugin{

    public String rutaConfig;
    private YamlConfiguration messages = null;
    private File messagesFile = null;
    public final PluginDescriptionFile pdffile = getDescription();
    public final String version = pdffile.getVersion();
    public String nombre = ChatColor.YELLOW+"["+ChatColor.GREEN+pdffile.getName()+ChatColor.YELLOW+"] "+ChatColor.GRAY;
    public static boolean isSpigot = true;

    @Override
    public void onEnable() {
        registerprefix();
        Bukkit.getConsoleSender().sendMessage(nombre + "has been enabled");
        registerMessages();
        registrosdecomandos();
        registrareventos();
        registerConfig();
        checkMissingConfigKeys();

        if (isRunningOnSpigot()) {
            Bukkit.getConsoleSender().sendMessage(nombre + "Using Spigot version...");
        } else {
            Bukkit.getConsoleSender().sendMessage(nombre + "Using Paper version...");
            isSpigot = false;
        }


        new Metrics(this,19294);

        new UpdateChecker(this, 111553).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(nombre + "There is not a new update available.");
            } else {
                Bukkit.getConsoleSender().sendMessage(nombre + "There is a new update available.");
            }
        });
    }


    public static boolean isRunningOnSpigot() {
        return !Bukkit.getServer().getName().equalsIgnoreCase("Paper");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(nombre + "has been disabled");
    }

    public void registrosdecomandos(){
        getCommand("villagerfollow").setExecutor(new VillagerFollowCommand(this));
    }

    public void registrareventos() {
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

    public void registerprefix(){
        FileConfiguration messagesConfig = getMessages();
        nombre = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("prefix") +  " §7");
    }

    private void checkMissingConfigKeys() {
        FileConfiguration config = getConfig();

        boolean modificado = false;

        if (!config.isSet("Config.check-update")) {
            config.set("Config.check-update", true);
            modificado = true;
        }

        if (!config.isSet("Config.villager-follow")) {
            config.set("Config.villager-follow", true);
            modificado = true;

        }

        if (!config.isSet("Config.villager-follow-player")) {
            config.set("Config.villager-follow-player", true);
            modificado = true;

        }

        if (!config.isSet("Config.villager-follow-radius")) {
            config.set("Config.villager-follow-radius", 12);
            modificado = true;

        }

        if (!config.isSet("Config.villager-allow-goto-emerald")) {
            config.set("Config.villager-allow-goto-emerald", true);
            modificado = true;

        }

        if (!config.isSet("Config.villager-allow-catch-emerald")) {
            config.set("Config.villager-allow-catch-emerald", true);
            modificado = true;

        }

        if (!config.isSet("Config.villager-pay-follow")) {
            config.set("Config.villager-pay-follow", true);
            modificado = true;

        }

        if (!config.isSet("Config.villager-pay-follow-time")) {
            config.set("Config.villager-pay-follow-time", 120);
            modificado = true;

        }

        if (!config.isSet("Config.villager-allow-catch-emerald-heal")) {
            config.set("Config.villager-allow-catch-emerald-heal", true);
            modificado = true;

        }

        if (modificado) {
            saveConfig();
        }
    }

}