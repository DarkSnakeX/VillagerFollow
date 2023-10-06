package me.darksnakex.villagerfollow.interactions;

import me.darksnakex.villagerfollow.VillagerFollow;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;


import static me.darksnakex.villagerfollow.interactions.VillagerInteraction.followThing;

public class VillagerInteractionGiveEmerald implements Listener {

    private static VillagerFollow plugin;

    public VillagerInteractionGiveEmerald(VillagerFollow plugin) {
        VillagerInteractionGiveEmerald.plugin = plugin;
    }

    private static int timeallowed = 60;
    private static boolean cancel = false;


    @EventHandler
    private void playerGiveEmerald(PlayerInteractEntityEvent event) {

        FileConfiguration config = plugin.getConfig();
        FileConfiguration messagesConfig = plugin.getMessages();
        if (!Objects.equals(config.getString("Config.villager-pay-follow"), "false") &&
                !Objects.equals(config.getString("Config.villager-follow"), "false")) {
            if (event.getRightClicked() instanceof Villager) {
                Villager villager = (Villager) event.getRightClicked();

                if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMERALD && !villager.hasMetadata("paid")) {
                    ItemStack esmeralda = new ItemStack(Material.EMERALD);
                    double velocidad = config.getDouble("Config.villager-follow-speed");
                    event.getPlayer().getInventory().removeItem(esmeralda);
                    villager.setMetadata("paid", new FixedMetadataValue(plugin, true));
                    villager.setMetadata(event.getPlayer().getDisplayName(),new FixedMetadataValue(plugin, true));
                    timeallowed = Math.abs(config.getInt("Config.villager-pay-follow-time"));
                    event.getPlayer().sendMessage(plugin.nombre + messagesConfig.getString("villager-pay.start") + timeallowed + "s.");
                    villager.playEffect(EntityEffect.VILLAGER_HAPPY);

                    new BukkitRunnable() {
                        int timepassed = 0;
                        @Override
                        public void run() {
                            if (timepassed >= timeallowed || cancel) {
                                event.getPlayer().sendMessage(plugin.nombre + messagesConfig.getString("villager-pay.end"));
                                villager.removeMetadata("paid", plugin);
                                villager.removeMetadata(event.getPlayer().getDisplayName(),plugin);
                                cancel = false;
                                cancel();
                                return;
                            }
                            followThing(villager,event.getPlayer().getLocation(), velocidad);
                            timepassed++;
                        }
                    }.runTaskTimer(plugin, 0, 20);
                }
            }
        }
    }

    @EventHandler
    private void playerCancelFollowPay(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof Villager && event.getPlayer().isSneaking() &&
            event.getRightClicked().hasMetadata("paid") &&
                event.getRightClicked().hasMetadata(event.getPlayer().getDisplayName())){
                cancel= true;


        }

    }


}
