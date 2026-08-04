package me.darksnakex.villagerfollow.interactions;

import me.darksnakex.villagerfollow.VillagerFollow;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import java.lang.reflect.Method;

public class VillagerInteraction {


    public static void followThing(VillagerFollow villagerFollow, Villager villager, Location location, double velocidad) {
        double locationVillager = villager.getLocation().distance(location);
        if (locationVillager > 25.0D) {
            teleportToNearestGround(villager, location);
        } else if (!(locationVillager < 3.00)) {
            if (!VillagerFollow.isSpigot) {
                try {
                    Class<?> paperPathfinderClass = Class.forName("com.destroystokyo.paper.entity.PaperPathfinder");
                    Method getPathfinderMethod = villager.getClass().getMethod("getPathfinder");
                    Object pathfinder = getPathfinderMethod.invoke(villager);
                    Method moveToMethod = paperPathfinderClass.getMethod("moveTo", Location.class, double.class);
                    moveToMethod.invoke(pathfinder, location, velocidad);
                } catch (ClassNotFoundException e) {
                    villagerFollow.getLogger().warning("Class PaperPathfinder not found. If you see this please report it");
                } catch (NoSuchMethodException e) {
                    villagerFollow.getLogger().warning("Method moveTo not found. If you see this please report it");
                } catch (Exception e) {
                    villagerFollow.getLogger().warning("Unknown error. If you see this please report it");
                }
            } else {
                EntityController controller = BukkitBrain.getBrain(villager).getController();
                controller.moveTo(location, velocidad);
                controller.lookAt(location);
            }
        }
    }

    private static void teleportToNearestGround(Villager villager, Location location) {
        Location groundLocation = location.clone();
        while (!groundLocation.getBlock().getType().isSolid()) {
            groundLocation.subtract(0, 1, 0);
            if (groundLocation.getY() < 0) {
                break;
            }
        }

        villager.teleport(groundLocation.add(0, 1, 0));
    }


}
