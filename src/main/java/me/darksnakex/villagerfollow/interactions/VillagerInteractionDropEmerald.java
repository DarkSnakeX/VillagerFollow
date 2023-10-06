package me.darksnakex.villagerfollow.interactions;

import me.darksnakex.villagerfollow.VillagerFollow;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

import static me.darksnakex.villagerfollow.interactions.VillagerInteraction.followThing;
import static me.darksnakex.villagerfollow.interactions.VillagerInteractionHandEmerald.radius;

public class VillagerInteractionDropEmerald implements Listener {

    private static VillagerFollow plugin;
    private static BukkitTask task;

    public VillagerInteractionDropEmerald(VillagerFollow plugin) {
        VillagerInteractionDropEmerald.plugin = plugin;
    }

    public static void onPluginReload2() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }



    @EventHandler
    private static void villagerFollowsEmerald(PlayerDropItemEvent event) {
        FileConfiguration config = plugin.getConfig();
        if(Objects.equals(config.getString("Config.villager-allow-goto-emerald"), "true") &&
            Objects.equals(config.getString("Config.villager-follow"), "true")) {
            Location playerLocation = event.getPlayer().getLocation();
            radius = config.getDouble("Config.villager-follow-radius");
            double velocidad = config.getDouble("Config.villager-follow-speed");
            if (task == null || task.isCancelled()) {
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity entity : playerLocation.getWorld().getEntities()) {
                            if (entity.getType().equals(EntityType.VILLAGER) && !entity.hasMetadata("paid")) {
                                Villager villager = (Villager) entity;
                                Location villagerLocation = villager.getLocation();

                                for (Entity itemEntity : villagerLocation.getWorld().getNearbyEntities(villagerLocation, radius, radius, radius)) {
                                    if (itemEntity instanceof Item && ((Item) itemEntity).getItemStack().getType() == Material.EMERALD) {
                                        double distance = villagerLocation.distance(itemEntity.getLocation());
                                        if (distance <= radius) {
                                            followThing(villager, itemEntity.getLocation(), velocidad);
                                        }
                                        if (distance <= 2 && Objects.equals(config.getString("Config.villager-allow-catch-emerald"), "true")) {
                                            ItemStack esmeralda = new ItemStack(Material.EMERALD, 1);
                                            villager.getEquipment().setItemInMainHand(esmeralda);
                                            itemEntity.remove();
                                            if(Objects.equals(config.getString("Config.villager-allow-catch-emerald-heal"), "true")){
                                                villager.setHealth(20);
                                                villager.playEffect(EntityEffect.VILLAGER_HEART);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
            }
        }
    }


}

