package me.darksnakex.villagerfollow.interactions;

import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

public class VillagerInteraction {


    public static void followThing(Villager villager, Location location, double velocidad){
        double distance = villager.getLocation().distance(location);

        if (distance > 25) {
            teleportToNearestGround(villager, location);
        } else if(!(distance < 2) ){
            BukkitBrain.getBrain(villager).getController().moveTo(location, velocidad).lookAt(location);
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
