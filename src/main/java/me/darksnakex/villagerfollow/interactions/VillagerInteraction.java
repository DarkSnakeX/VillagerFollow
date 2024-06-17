package me.darksnakex.villagerfollow.interactions;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

public class VillagerInteraction {



        public static void followThing(Villager villager, Location location, double velocidad) {
            double locvil = villager.getLocation().distance(location);
            if (locvil > 25.0D) {
                teleportToNearestGround(villager, location);
            } else if (!(locvil < 3.00)) {
                EntityBrain brain = BukkitBrain.getBrain(villager);
                EntityController controller = brain.getController();
                controller.moveTo(location, velocidad);
                controller.lookAt(location);
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
