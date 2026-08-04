import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.darksnakex.villagerfollow.VillagerFollow;
import me.darksnakex.villagerfollow.interactions.VillagerInteractionHandEmerald;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class VillagerInteractionHandEmeraldTest {

    private ServerMock server;
    private VillagerFollow plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(VillagerFollow.class);
        server.getPluginManager().registerEvents(new VillagerInteractionHandEmerald(plugin), plugin);
    }

    @AfterEach
    void tearDown() {
        VillagerInteractionHandEmerald.onPluginReload();
        MockBukkit.unmock();
    }

    @Test
    void testTaskStartsWhenHoldingEmerald() throws Exception {

        PlayerMock player = server.addPlayer();

        player.getInventory().setItem(1, new ItemStack(Material.EMERALD));

        PlayerItemHeldEvent event =
                new PlayerItemHeldEvent(player, 0, 1);

        server.getPluginManager().callEvent(event);

        Field field = VillagerInteractionHandEmerald.class.getDeclaredField("task");
        field.setAccessible(true);

        assertNotNull(field.get(null), "La tarea debería haberse creado");
    }

    @Test
    void testTaskStopsWhenNotHoldingEmerald() throws Exception {

        PlayerMock player = server.addPlayer();

        player.getInventory().setItem(1, new ItemStack(Material.EMERALD));

        server.getPluginManager().callEvent(
                new PlayerItemHeldEvent(player, 0, 1));

        player.getInventory().setItem(2, new ItemStack(Material.STONE));

        server.getPluginManager().callEvent(
                new PlayerItemHeldEvent(player, 1, 2));

        Field field = VillagerInteractionHandEmerald.class.getDeclaredField("task");
        field.setAccessible(true);

        assertTrue(((org.bukkit.scheduler.BukkitTask) field.get(null)).isCancelled());
    }
}