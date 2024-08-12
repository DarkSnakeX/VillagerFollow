package me.darksnakex.villagerfollow.interactions;

import me.darksnakex.villagerfollow.VillagerFollow;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import java.lang.reflect.Method;

public class VillagerInteraction {



        public static void followThing(Villager villager, Location location, double velocidad) {
            double locvil = villager.getLocation().distance(location);
            if (locvil > 25.0D) {
                teleportToNearestGround(villager, location);
            } else if (!(locvil < 3.00)) {
                if (!VillagerFollow.isSpigot) {
                    try {
                        Class<?> paperPathfinderClass = Class.forName("com.destroystokyo.paper.entity.PaperPathfinder");
                        Method getPathfinderMethod = villager.getClass().getMethod("getPathfinder");
                        Object pathfinder = getPathfinderMethod.invoke(villager);
                        Method moveToMethod = paperPathfinderClass.getMethod("moveTo", Location.class, double.class);
                        moveToMethod.invoke(pathfinder, location, velocidad);
                    } catch (ClassNotFoundException e) {
                        Bukkit.getLogger().warning( "Class PaperPathfinder not found. If you see this please report it");
                    } catch (NoSuchMethodException e) {
                        Bukkit.getLogger().warning("Method moveTo not found. If you see this please report it");
                    } catch (Exception e) {
                        Bukkit.getLogger().warning("Unknown error. If you see this please report it");
                        e.printStackTrace();
                    }
                } else {
                    EntityController controller = BukkitBrain.getBrain(villager).getController();
                    controller.moveTo(location, velocidad);
                    controller.lookAt(location);
                }
            }
        }

    private static void teleportToNearestGround(Villager villager, Location location) {

        // Find the nearest solid ground below the specified location
        Location groundLocation = location.clone();
        while (!groundLocation.getBlock().getType().isSolid()) {
            groundLocation.subtract(0, 1, 0);

            // Break the loop if the ground is too far below
            if (groundLocation.getY() < 0) {
                break;
            }
        }

        // Teleport the villager to the nearest solid ground
        villager.teleport(groundLocation.add(0, 1, 0));
    }


}
