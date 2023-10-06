package me.darksnakex.villagerfollow.interactions;

import me.darksnakex.villagerfollow.VillagerFollow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import static me.darksnakex.villagerfollow.interactions.VillagerInteraction.followThing;

public class VillagerInteractionHandEmerald implements Listener {
    private static VillagerFollow plugin;
    private static BukkitTask task;

    public VillagerInteractionHandEmerald(VillagerFollow plugin) {
        VillagerInteractionHandEmerald.plugin = plugin;
    }
    public static double radius;

    @EventHandler
    public void villagerFollowsPlayer(PlayerItemHeldEvent event) {
        FileConfiguration config = plugin.getConfig();
        String villagerFollow = config.getString("Config.villager-follow");
        String followPlayer = config.getString("Config.villager-follow-player");

        if ("true".equals(villagerFollow) && "true".equals(followPlayer)) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItem(event.getNewSlot());
            World world = player.getWorld();

            if (item != null && (item.getType() == Material.EMERALD)) {
                double radius = config.getDouble("Config.villager-follow-radius");
                double velocidad = config.getDouble("Config.villager-follow-speed");

                if (task == null || task.isCancelled()) {
                    task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location target = player.getLocation();

                            for (Entity entity : world.getNearbyEntities(target, radius, radius, radius)) {
                                if (entity.getType() == EntityType.VILLAGER && !entity.hasMetadata("paid")) {
                                    Villager villager = (Villager) entity;
                                    followThing(villager, target, velocidad);
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 20);
                }
            } else if (task != null) {
                task.cancel();
            }
        }
    }




    public static void onPluginReload() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }



}
